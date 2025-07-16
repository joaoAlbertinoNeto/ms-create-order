terraform {
  required_providers {
    kubernetes = {
      source  = "hashicorp/kubernetes"
      version = ">= 2.22.0"
    }
    helm = {
      source  = "hashicorp/helm"
      version = ">= 2.12.0"
    }
  }
}

provider "kubernetes" {
  config_path = "~/.kube/config"
}

provider "helm" {
  kubernetes {
    config_path = "~/.kube/config"
  }
}

resource "helm_release" "strimzi_operator" {
  name       = "strimzi-kafka-operator"
  repository = "https://strimzi.io/charts/"
  chart      = "strimzi-kafka-operator"
  namespace  = "default"
  version    = "0.46.1"
}

resource "kubernetes_manifest" "ms_create_order_deployment" {
  manifest = yamldecode(file("./k8s/ms-create-order-deployment.yaml"))
}

resource "kubernetes_manifest" "ms_create_order_service" {
  manifest = yamldecode(file("./k8s/ms-create-order-service.yaml"))
}

resource "kubernetes_manifest" "kafka_topic_pedidos" {
  manifest = yamldecode(file("./k8s/kafka-topic-pedidos.yaml"))
}
