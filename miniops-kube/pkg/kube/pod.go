package kube

import (
    "context"
    "errors"
    "go.uber.org/zap"
    "io"
    corev1 "k8s.io/api/core/v1"
    "k8s.io/utils/ptr"
)

func PodLog(ctx context.Context, namespace, podName, containerName string) (io.ReadCloser, error) {
    opts := &corev1.PodLogOptions{
        Container: containerName,
        TailLines: ptr.To(int64(1)),
        Follow:    true,
    }

    stream, err := clientSet().CoreV1().
        Pods(namespace).
        GetLogs(podName, opts).
        Stream(ctx)

    if err != nil {
        zap.L().Error("failed to get pod log", zap.Error(err))
        return nil, errors.New("failed to get pod log")
    }

    return stream, nil
}
