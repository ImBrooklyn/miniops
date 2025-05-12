package kube

import (
    "context"
    "errors"
    "go.uber.org/zap"
    corev1 "k8s.io/api/core/v1"
    metav1 "k8s.io/apimachinery/pkg/apis/meta/v1"
)

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
