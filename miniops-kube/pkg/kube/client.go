package kube

import (
    "k8s.io/client-go/kubernetes"
    "k8s.io/client-go/tools/clientcmd"
    "sync"
)

var (
    c    *kubernetes.Clientset
    once sync.Once
)

func clientSet() *kubernetes.Clientset {
    once.Do(func() {
        config, err := clientcmd.BuildConfigFromFlags("", clientcmd.RecommendedHomeFile)
        if err != nil {
            panic(err)
        }

        if cs, err := kubernetes.NewForConfig(config); err != nil {
            panic(err)
        } else {
            c = cs
        }
    })
    return c
}
