# MiniOps

### Project Overview

&zwnj;**MiniOps**&zwnj; is a lightweight DevOps platform demonstration designed for educational purposes and technical experimentation. This project provides a simplified implementation of cloud-native concepts including configuration management, application orchestration, and infrastructure automation.

&zwnj;**Note**&zwnj;: This is &zwnj;**not production-ready software**&zwnj; - it serves as:

- A learning tool for DevOps patterns
- A reference implementation for educational workshops
- A technical demonstration of cloud-native concepts
- And last but not least, pilot study for AIOps

---

### Architecture Components

```text
miniops
├── miniops-busybox-j       # Trivial application, for demonstration and experimentation
├── miniops-common          # Shared components: Base classes, utilities and patterns
├── miniops-confer          # Configuration Center Server (centralized configuration service)
├── miniops-confer-client   # Configuration Client SDK for Java (integration library)
├── miniops-confer-common   # Configuration Commons (DTOs, enums, constants)
├── miniops-ctl             # CLI Client (command-line interface for operations)
├── miniops-kube            # Kubernetes Operator (cluster orchestration engine)
└── miniops-warehouse       # Configuration management database, i.e. CMDB
```


