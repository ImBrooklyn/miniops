package kube

import (
    "context"
    "errors"
    "fmt"
    "github.com/ImBrooklyn/miniops/miniops-kube/internal/model/errs"
    "go.uber.org/zap"
    appsv1 "k8s.io/api/apps/v1"
    corev1 "k8s.io/api/core/v1"
    networkingv1 "k8s.io/api/networking/v1"
    metav1 "k8s.io/apimachinery/pkg/apis/meta/v1"
    "k8s.io/apimachinery/pkg/util/intstr"
    "k8s.io/utils/ptr"
    "strings"
)

func CreateConfig(ctx context.Context, d *Deployment) error {
    if d == nil || d.AppConfig == nil {
        zap.S().Errorf("nil param: %v", d)
        return errors.New("nil param")
    }
    var configProps []string
    var secretProps []string
    for _, prop := range d.AppConfig.ConfigProperties {
        line := fmt.Sprintf("%s=%s", prop.PropKey, prop.PropValue)
        if prop.IsSecret {
            secretProps = append(secretProps, line)
        } else {
            configProps = append(configProps, line)
        }
    }

    configmapData := map[string]string{
        "configmap.properties": strings.Join(configProps, "\n"),
    }
    if err := ApplyConfigMap(ctx, d.ConfigmapName(), d.Namespace(), configmapData); err != nil {
        return err
    }

    secretData := map[string]string{
        "secret.properties": strings.Join(secretProps, "\n"),
    }

    if err := ApplySecret(ctx, d.SecretName(), d.Namespace(), secretData); err != nil {
        return err
    }

    if len(secretProps) > 0 {

    }

    resources := d.AppConfig.Resources
    resourceData := make(map[string]string)
    for _, resource := range resources {
        if len(resource.ResourceProperties) == 0 {
            continue
        }
        var resourceProps []string
        for _, prop := range resource.ResourceProperties {
            line := fmt.Sprintf("%s=%s", prop.PropKey, prop.PropValue)
            resourceProps = append(resourceProps, line)
        }

        dataName := fmt.Sprintf("%s.properties", strings.ToLower(resource.Type))
        resourceData[dataName] = strings.Join(resourceProps, "\n")
    }

    if err := ApplySecret(ctx, d.ResourceName(), d.Namespace(), resourceData); err != nil {
        return err
    }

    return nil
}

func ApplyDeployment(ctx context.Context, d *Deployment) error {
    if d == nil || d.Deployment == nil || d.Artifact == nil || d.App == nil {
        return errors.New("nil param")
    }

    zap.S().Infof("applying deployment: %v", d.Deployment)

    services := d.App.Services
    var ports []corev1.ContainerPort
    for _, r := range services {
        ports = append(ports, corev1.ContainerPort{
            ContainerPort: int32(r.Port),
        })
    }

    liveness := d.App.Liveness
    livenessParam := ParseLivenessParams(liveness.Params)
    readiness := d.App.Readiness
    readinessParam := ParseReadinessParams(readiness.Params)

    repo := "miniops-snapshot"
    if d.Artifact.Prerelease == "" {
        repo = "miniops-release"
    }

    container := corev1.Container{
        Name:  fmt.Sprintf("%s-container", d.Name()),
        Image: fmt.Sprintf("%s/%s:%s", repo, d.App.Name, d.Artifact.Version),
        Ports: ports,
        Env: []corev1.EnvVar{
            {
                Name:  "MINIOPS_ENV",
                Value: d.Namespace(),
            },
            {
                Name:  "MINIOPS_APP_NAME",
                Value: d.Deployment.AppName,
            },
            {
                Name:  "MINIOPS_DEPLOYMENT_NAME",
                Value: d.Name(),
            },
            {
                Name:  "MINIOPS_DEPLOYMENT_SUFFIX",
                Value: d.Deployment.Suffix,
            },
            {
                Name:  "ENABLE_CONFER",
                Value: "TRUE",
            },
            {
                Name:  "CONFER_DIR",
                Value: conferDir,
            },
            {
                Name:  "CONFIGMAP_DIR",
                Value: configmapDir,
            },
            {
                Name:  "SECRET_DIR",
                Value: secretDir,
            },
            {
                Name:  "RESOURCE_DIR",
                Value: resourceDir,
            },
        },

        LivenessProbe: &corev1.Probe{
            ProbeHandler: corev1.ProbeHandler{
                HTTPGet: &corev1.HTTPGetAction{
                    Path: liveness.Path,
                    Port: intstr.FromInt32(int32(liveness.Port)),
                },
            },
            InitialDelaySeconds: livenessParam.InitialDelaySeconds,
            TimeoutSeconds:      livenessParam.TimeoutSeconds,
            PeriodSeconds:       livenessParam.PeriodSeconds,
            SuccessThreshold:    livenessParam.SuccessThreshold,
            FailureThreshold:    livenessParam.FailureThreshold,
        },

        ReadinessProbe: &corev1.Probe{
            ProbeHandler: corev1.ProbeHandler{
                HTTPGet: &corev1.HTTPGetAction{
                    Path: readiness.Path,
                    Port: intstr.FromInt32(int32(readiness.Port)),
                },
            },
            InitialDelaySeconds: readinessParam.InitialDelaySeconds,
            PeriodSeconds:       readinessParam.PeriodSeconds,
        },

        VolumeMounts: []corev1.VolumeMount{
            {
                Name:      configmapVolume,
                MountPath: fmt.Sprintf("%s/%s", conferDir, configmapDir),
            },
            {
                Name:      secretVolume,
                MountPath: fmt.Sprintf("%s/%s", conferDir, secretDir),
            },
            {
                Name:      resourceVolume,
                MountPath: fmt.Sprintf("%s/%s", conferDir, resourceDir),
            },
        },
    }

    volumes := []corev1.Volume{
        {
            Name: configmapVolume,
            VolumeSource: corev1.VolumeSource{
                ConfigMap: &corev1.ConfigMapVolumeSource{
                    LocalObjectReference: corev1.LocalObjectReference{
                        Name: d.ConfigmapName(),
                    },
                },
            },
        },
        {
            Name: secretVolume,
            VolumeSource: corev1.VolumeSource{
                Secret: &corev1.SecretVolumeSource{
                    SecretName: d.SecretName(),
                },
            },
        },
        {
            Name: resourceVolume,
            VolumeSource: corev1.VolumeSource{
                Secret: &corev1.SecretVolumeSource{
                    SecretName: d.ResourceName(),
                },
            },
        },
    }

    deployment := &appsv1.Deployment{
        ObjectMeta: metav1.ObjectMeta{
            Name: d.Name(),
        },
        Spec: appsv1.DeploymentSpec{
            Replicas: ptr.To(int32(d.Deployment.Replicas)),
            Selector: &metav1.LabelSelector{
                MatchLabels: map[string]string{
                    "app": d.Name(),
                },
            },
            Template: corev1.PodTemplateSpec{
                ObjectMeta: metav1.ObjectMeta{
                    Labels: map[string]string{
                        "app": d.Name(),
                    },
                },
                Spec: corev1.PodSpec{
                    Containers: []corev1.Container{container},
                    Volumes:    volumes,
                },
            },
        },
    }

    current, err := GetDeployment(ctx, d)
    if err == nil && current != nil {
        _, err = clientSet().AppsV1().
            Deployments(d.Namespace()).
            Update(ctx, deployment, metav1.UpdateOptions{})
    } else {
        _, err = clientSet().AppsV1().
            Deployments(d.Namespace()).
            Create(ctx, deployment, metav1.CreateOptions{})
    }

    if err != nil {
        zap.L().Error("failed to apply deployment", zap.Error(err))
        return err
    }

    return nil
}

func ExposeDeployment(ctx context.Context, d *Deployment) error {
    if d == nil || d.Deployment == nil || d.App == nil {
        zap.S().Errorf("nil param: %v", d)
        return errors.New("nil param")
    }

    _, err := GetDeployment(ctx, d)
    if err != nil {
        zap.L().Error("failed to get deployment", zap.Error(err))
        return err
    }

    services := d.App.Services
    var ports []corev1.ServicePort
    for _, r := range services {
        ports = append(ports, corev1.ServicePort{
            Port:       int32(r.Port),
            TargetPort: intstr.FromInt32(int32(r.Port)),
        })
    }

    service := &corev1.Service{
        TypeMeta: metav1.TypeMeta{},
        ObjectMeta: metav1.ObjectMeta{
            Name: d.ServiceName(),
        },
        Spec: corev1.ServiceSpec{
            Selector: map[string]string{
                "app": d.Name(),
            },
            Ports: ports,
            Type:  corev1.ServiceTypeClusterIP,
        },
    }

    currentService, err := clientSet().CoreV1().
        Services(d.Namespace()).
        Get(ctx, d.ServiceName(), metav1.GetOptions{})
    if err == nil && currentService != nil {
        _, err = clientSet().CoreV1().
            Services(d.Namespace()).
            Update(ctx, service, metav1.UpdateOptions{})
    } else {
        _, err = clientSet().CoreV1().
            Services(d.Namespace()).
            Create(ctx, service, metav1.CreateOptions{})
    }

    if err != nil {
        zap.L().Error("failed to create/update service", zap.Error(err))
        return errors.New("failed to create/update service")
    }

    var paths []networkingv1.HTTPIngressPath
    for _, r := range services {
        ingressPath := networkingv1.HTTPIngressPath{
            Path:     r.Path,
            PathType: ptr.To(networkingv1.PathTypePrefix),
            Backend: networkingv1.IngressBackend{
                Service: &networkingv1.IngressServiceBackend{
                    Name: d.ServiceName(),
                    Port: networkingv1.ServiceBackendPort{
                        Number: int32(r.Port),
                    },
                },
            },
        }
        paths = append(paths, ingressPath)
    }

    ingress := &networkingv1.Ingress{
        ObjectMeta: metav1.ObjectMeta{
            Name: d.IngressName(),
            Annotations: map[string]string{
                "nginx.ingress.kubernetes.io/proxy-body-size": "50m",
            },
        },
        Spec: networkingv1.IngressSpec{
            Rules: []networkingv1.IngressRule{
                {
                    Host: d.IngressHost(),
                    IngressRuleValue: networkingv1.IngressRuleValue{
                        HTTP: &networkingv1.HTTPIngressRuleValue{
                            Paths: paths,
                        },
                    },
                },
            },
        },
    }

    currentIngress, err := clientSet().NetworkingV1().
        Ingresses(d.Namespace()).
        Get(ctx, d.IngressName(), metav1.GetOptions{})

    if err == nil && currentIngress != nil {
        _, err = clientSet().NetworkingV1().
            Ingresses(d.Namespace()).
            Update(ctx, ingress, metav1.UpdateOptions{})
    } else {
        _, err = clientSet().NetworkingV1().
            Ingresses(d.Namespace()).
            Create(ctx, ingress, metav1.CreateOptions{})
    }

    if err != nil {
        zap.L().Error("failed to create/update ingress", zap.Error(err))
        return errors.New("failed to create/update ingress")
    }

    return nil

}

func GetDeployment(ctx context.Context, d *Deployment) (*appsv1.Deployment, error) {
    deployment, err := clientSet().AppsV1().
        Deployments(d.Namespace()).
        Get(ctx, d.Name(), metav1.GetOptions{})
    if err != nil {
        zap.S().Errorf("Get deployment failed, deployment: %s, err: %s", d.Name(), err)
        return nil, fmt.Errorf("get deployment error")
    }

    return deployment, nil
}

func GetService(ctx context.Context, d *Deployment) (*corev1.Service, error) {
    service, err := clientSet().CoreV1().
        Services(d.Namespace()).
        Get(ctx, d.ServiceName(), metav1.GetOptions{})
    if err != nil {
        zap.S().Errorf("get service failed, deployment: %s, err: %s", d.Name(), err)
        return nil, fmt.Errorf("failed to get service")
    }
    return service, nil
}

func GetIngress(ctx context.Context, d *Deployment) (*networkingv1.Ingress, error) {
    ingress, err := clientSet().NetworkingV1().
        Ingresses(d.Namespace()).
        Get(ctx, d.IngressName(), metav1.GetOptions{})
    if err != nil {
        zap.S().Errorf("get ingress failed, deployment: %s, err: %s", d.Name(), err)
        return nil, fmt.Errorf("failed to get ingress")
    }

    return ingress, nil
}

func ScaleDeployment(ctx context.Context, d *Deployment, replicas int) error {
    return updateDeployment(ctx, d, func(d *appsv1.Deployment) error {
        if *d.Spec.Replicas == int32(replicas) {
            return errs.ErrNotModified
        }
        d.Spec.Replicas = ptr.To(int32(replicas))
        return nil
    })
}

func DeleteDeployment(ctx context.Context, d *Deployment) error {
    _, err := GetDeployment(ctx, d)
    if err != nil {
        return err
    }

    err = clientSet().AppsV1().
        Deployments(d.Namespace()).
        Delete(ctx, d.Name(), metav1.DeleteOptions{})
    if err != nil {
        zap.S().Errorf("delete deployment failed, deployment: %s, err: %s", d.Name(), err)
        return fmt.Errorf("delete deployment error")
    }
    return nil
}

func DeleteDeploymentSvc(ctx context.Context, d *Deployment) error {
    if d == nil || d.Deployment == nil || d.App == nil {
        zap.S().Errorf("nil param: %v", d)
        return errors.New("nil param")
    }

    _, err := GetDeployment(ctx, d)
    if err != nil {
        zap.L().Error("failed to get deployment", zap.Error(err))
        return err
    }

    err = clientSet().NetworkingV1().Ingresses(d.Namespace()).
        Delete(ctx, d.IngressName(), metav1.DeleteOptions{})
    if err != nil {
        zap.L().Error("failed to delete ingress", zap.Error(err))
        return errors.New("failed to delete ingress")
    }

    err = clientSet().CoreV1().
        Services(d.Namespace()).
        Delete(ctx, d.ServiceName(), metav1.DeleteOptions{})

    if err != nil {
        zap.L().Error("failed to delete service", zap.Error(err))
        return errors.New("failed to delete service")
    }
    return nil
}

func ApplyConfigMap(ctx context.Context, name, namespace string, data map[string]string) error {
    configMap := &corev1.ConfigMap{
        ObjectMeta: metav1.ObjectMeta{
            Name: name,
        },
        Data: data,
    }

    currentConfigmap, err := clientSet().CoreV1().
        ConfigMaps(namespace).
        Get(ctx, name, metav1.GetOptions{})
    zap.L().Error("failed to get configmap", zap.Error(err))
    if err == nil && currentConfigmap != nil {
        _, err = clientSet().CoreV1().
            ConfigMaps(namespace).
            Update(ctx, configMap, metav1.UpdateOptions{})
    } else {
        _, err = clientSet().CoreV1().
            ConfigMaps(namespace).
            Create(ctx, configMap, metav1.CreateOptions{})
    }

    if err != nil {
        zap.L().Error("failed to apply configmap", zap.Error(err))
        return errors.New("failed to apply configmap")
    }

    return nil
}

func ApplySecret(ctx context.Context, name, namespace string, data map[string]string) error {
    secret := &corev1.Secret{
        ObjectMeta: metav1.ObjectMeta{
            Name: name,
        },
        StringData: data,
    }

    currentSecret, err := clientSet().CoreV1().
        Secrets(namespace).
        Get(ctx, name, metav1.GetOptions{})
    zap.L().Error("failed to get configmap", zap.Error(err))
    if err == nil && currentSecret != nil {
        _, err = clientSet().CoreV1().
            Secrets(namespace).
            Update(ctx, secret, metav1.UpdateOptions{})
    } else {
        _, err = clientSet().CoreV1().
            Secrets(namespace).
            Create(ctx, secret, metav1.CreateOptions{})
    }

    if err != nil {
        zap.L().Error("failed to apply secret", zap.Error(err))
        return errors.New("failed to apply secret")
    }

    return nil
}

func updateDeployment(ctx context.Context, d *Deployment, patchFunc func(*appsv1.Deployment) error) error {
    current, err := GetDeployment(ctx, d)
    if err != nil {
        return err
    }

    if err := patchFunc(current); err != nil {
        if errors.Is(err, errs.ErrNotModified) {
            return nil
        }
        return err
    }

    _, err = clientSet().AppsV1().
        Deployments(d.Namespace()).
        Update(ctx, current, metav1.UpdateOptions{})

    if err != nil {
        zap.S().Errorf("Update deployment failed, namespace: %s, deployment: %s, err: %s",
            current.Namespace, current.Name, err)
        return fmt.Errorf("update deployment error")
    }
    return nil
}
