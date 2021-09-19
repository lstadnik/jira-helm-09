# bitbucket

![Version: 0.9.0](https://img.shields.io/badge/Version-0.9.0-informational?style=flat-square) ![Type: application](https://img.shields.io/badge/Type-application-informational?style=flat-square) ![AppVersion: 7.12.1-jdk11](https://img.shields.io/badge/AppVersion-7.12.1--jdk11-informational?style=flat-square)

A chart for installing Bitbucket Data Center on Kubernetes

**Homepage:** <https://www.atlassian.com/software/bitbucket>

## Source Code

* <https://github.com/atlassian-labs/data-center-helm-charts>
* <https://bitbucket.org/atlassian-docker/docker-atlassian-bitbucket-server/>

## Requirements

Kubernetes: `>=1.17.x-0`

## Values

| Key | Type | Default | Description |
|-----|------|---------|-------------|
| additionalContainers | list | `[]` | Additional container definitions that will be added to all Bitbucket pods |
| additionalFiles | list | `[]` | Additional existing ConfigMaps and Secrets not managed by Helm, which should be mounted into server container configMap and secret are two available types (camelCase is important!) mountPath is a destination directory in a container, and key is the file name name references existing ConfigMap or secret name. VolumeMount and Volumes are added with this name and the index position, for example, custom-config-0, keystore-2 |
| additionalInitContainers | list | `[]` | Additional initContainer definitions that will be added to all Bitbucket pods |
| additionalLabels | object | `{}` | Additional labels that should be applied to all resources |
| affinity | object | `{}` | Standard Kubernetes affinities that will be applied to all Bitbucket pods Due to the performance requirements it is highly recommended to run all Bitbucket pods in the same availability zone as your dedicated NFS server. To achieve this, you can define `affinity` and `podAffinity` rules that will place all pods into the same zone, and therefore minimise the real distance between the application pods and the shared storage. More specific documentation can be found in the official Affinity and Anti-affinity documentation:  https://kubernetes.io/docs/concepts/scheduling-eviction/assign-pod-node/#affinity-and-anti-affinity This is an example on how to ensure the pods are in the same zone as NFS server that is labeled with `role=nfs-server`:   podAffinity:    requiredDuringSchedulingIgnoredDuringExecution:      - labelSelector:          matchExpressions:            - key: role              operator: In              values:                - nfs-server # needs to be the same value as NFS server deployment        topologyKey: topology.kubernetes.io/zone |
| bitbucket.additionalBundledPlugins | list | `[]` | Specifies a list of additional Bitbucket plugins that should be added to the Bitbucket container. These are specified in the same manner as the additionalLibraries field, but the files will be loaded as bundled plugins rather than as libraries. |
| bitbucket.additionalEnvironmentVariables | list | `[]` | Defines any additional environment variables to be passed to the Bitbucket container. See https://hub.docker.com/r/atlassian/bitbucket-server for supported variables. |
| bitbucket.additionalJvmArgs | list | `["-XX:ActiveProcessorCount=2"]` | Specifies a list of additional arguments that can be passed to the Bitbucket JVM, e.g. system properties |
| bitbucket.additionalJvmArgs[0] | string | `"-XX:ActiveProcessorCount=2"` | The value defined for ActiveProcessorCount should correspond to that provided for 'container.requests.cpu' see: https://docs.oracle.com/en/java/javase/11/tools/java.html#GUID-3B1CE181-CD30-4178-9602-230B800D4FAE |
| bitbucket.additionalLibraries | list | `[]` | Specifies a list of additional Java libraries that should be added to the Bitbucket container. Each item in the list should specify the name of the volume that contains the library, as well as the name of the library file within that volume's root directory. Optionally, a subDirectory field can be included to specify which directory in the volume contains the library file. |
| bitbucket.additionalVolumeMounts | list | `[]` | Defines any additional volumes mounts for the Bitbucket container. These can refer to existing volumes, or new volumes can be defined in volumes.additional. |
| bitbucket.clustering.enabled | bool | `false` | Set to true if Data Center clustering should be enabled This will automatically configure cluster peer discovery between cluster nodes. |
| bitbucket.elasticSearch.baseUrl | string | `nil` | The base URL of the external ElasticSearch instance to be used. If this is defined, then Bitbucket will disable its internal ElasticSearch instance. |
| bitbucket.elasticSearch.credentials.passwordSecretKey | string | `"password"` | The key in the the Kubernetes Secret that contains the ElasticSearch password. |
| bitbucket.elasticSearch.credentials.secretName | string | `nil` | The name of the Kubernetes Secret that contains the ElasticSearch credentials. |
| bitbucket.elasticSearch.credentials.usernameSecreyKey | string | `"username"` | The key in the the Kubernetes Secret that contains the ElasticSearch username. |
| bitbucket.license.secretKey | string | `"license-key"` | The key in the Kubernetes Secret that contains the Bitbucket license key |
| bitbucket.license.secretName | string | `nil` | The name of the Kubernetes Secret that contains the Bitbucket license key. If specified, then the license will be automatically populated during Bitbucket setup. Otherwise, it will need to be provided via the browser after initial startup. |
| bitbucket.ports.hazelcast | int | `5701` |  |
| bitbucket.ports.http | int | `7990` |  |
| bitbucket.ports.ssh | int | `7999` |  |
| bitbucket.resources.container | object | `{"requests":{"cpu":"2","memory":"2G"}}` | Specifies the standard Kubernetes resource requests and/or limits for the Bitbucket container. It is important that if the memory resources are specified here, they must allow for the size of the Bitbucket JVM. That means the maximum heap size, the reserved code cache size, plus other JVM overheads, must be accommodated. Allowing for maxHeap * 1.5 would be an example. |
| bitbucket.resources.jvm.maxHeap | string | `"1g"` | JVM memory arguments below are based on the defaults defined for the Bitbucket docker container, see: https://bitbucket.org/atlassian-docker/docker-atlassian-bitbucket-server/src/master/ -- The maximum amount of heap memory that will be used by the Bitbucket JVM. The same value will be used by the Elasticsearch JVM. |
| bitbucket.resources.jvm.minHeap | string | `"512m"` | The minimum amount of heap memory that will be used by the Bitbucket JVM. The same value will be used by the Elasticsearch JVM. |
| bitbucket.securityContext | object | `{"enabled":true,"gid":"2003"}` | Enable or disable security context in StatefulSet template spec. Enabled by default with UID 2003. -- Disable when deploying to OpenShift, unless anyuid policy is attached to service account |
| bitbucket.securityContext.gid | string | `"2003"` | The GID used by the Bitbucket docker image |
| bitbucket.service.port | int | `80` | The port on which the Bitbucket Kubernetes service will listen |
| bitbucket.service.type | string | `"ClusterIP"` | The type of Kubernetes service to use for Bitbucket |
| bitbucket.sysadminCredentials.displayNameSecretKey | string | `"displayName"` | The key in the Kubernetes Secret that contains the sysadmin display name |
| bitbucket.sysadminCredentials.emailAddressSecretKey | string | `"emailAddress"` | The key in the Kubernetes Secret that contains the sysadmin email address |
| bitbucket.sysadminCredentials.passwordSecretKey | string | `"password"` | The key in the Kubernetes Secret that contains the sysadmin password |
| bitbucket.sysadminCredentials.secretName | string | `nil` | The name of the Kubernetes Secret that contains the Bitbucket sysadmin credentials If specified, then these will be automatically populated during Bitbucket setup. Otherwise, they will need to be provided via the browser after initial startup. |
| bitbucket.sysadminCredentials.usernameSecretKey | string | `"username"` | The key in the Kubernetes Secret that contains the sysadmin username |
| database.credentials.passwordSecretKey | string | `"password"` | The key in the Secret used to store the database login password |
| database.credentials.secretName | string | `nil` | The name of the Kubernetes Secret that contains the database login credentials. If specified, then the credentials will be automatically populated during Bitbucket setup. Otherwise, they will need to be provided via the browser after initial startup. |
| database.credentials.usernameSecretKey | string | `"username"` | The key in the Secret used to store the database login username |
| database.driver | string | `nil` | The Java class name of the JDBC driver to be used, e.g. org.postgresql.Driver If not specified, then it will need to be provided via the browser during initial startup. |
| database.url | string | `nil` | The JDBC URL of the database to be used by Bitbucket, e.g. jdbc:postgresql://host:port/database If not specified, then it will need to be provided via the browser during initial startup. |
| fluentd.customConfigFile | bool | `false` | True if a custom config should be used for Fluentd |
| fluentd.elasticsearch.enabled | bool | `true` | True if Fluentd should send all log events to an Elasticsearch service. |
| fluentd.elasticsearch.hostname | string | `"elasticsearch"` | The hostname of the Elasticsearch service that Fluentd should send logs to. |
| fluentd.enabled | bool | `false` | True if the Fluentd sidecar should be added to each pod |
| fluentd.extraVolumes | list | `[]` | Specify custom volumes to be added to Fluentd container (e.g. more log sources) |
| fluentd.fluentdCustomConfig | object | `{}` | Custom fluent.conf file fluent.conf: | |
| fluentd.imageName | string | `"fluent/fluentd-kubernetes-daemonset:v1.11.5-debian-elasticsearch7-1.2"` | The name of the image containing the Fluentd sidecar |
| image.pullPolicy | string | `"IfNotPresent"` |  |
| image.repository | string | `"atlassian/bitbucket-server"` |  |
| image.tag | string | `""` | The docker image tag to be used. Defaults to the Chart appVersion. |
| ingress.annotations | object | `{}` | The custom annotations that should be applied to the Ingress  Resource when not using the Kubernetes ingress-nginx controller. |
| ingress.create | bool | `false` | True if an Ingress Resource should be created. |
| ingress.host | string | `nil` | The fully-qualified hostname of the Ingress Resource. |
| ingress.https | bool | `true` | True if the browser communicates with the application over HTTPS. |
| ingress.maxBodySize | string | `"250m"` | The max body size to allow. Requests exceeding this size will result in an 413 error being returned to the client. https://kubernetes.github.io/ingress-nginx/user-guide/nginx-configuration/annotations/#custom-max-body-size |
| ingress.nginx | bool | `true` | True if the created Ingress Resource is to use the Kubernetes ingress-nginx controller: https://kubernetes.github.io/ingress-nginx/ This will populate the Ingress Resource with annotations for the Kubernetes ingress-nginx controller. Set to false if a different controller is to be used, in which case the appropriate annotations for that controller need to be specified. |
| ingress.path | string | `"/"` | The base path for the ingress rule. |
| ingress.tlsSecretName | string | `nil` | Secret that contains a TLS private key and certificate. Optional if Ingress Controller is configured to use one secret for all ingresses |
| nodeSelector | object | `{}` | Standard Kubernetes node-selectors that will be applied to all Bitbucket pods |
| podAnnotations | object | `{}` | Specify custom annotations to be added to all Bitbucket pods |
| replicaCount | int | `1` | The initial number of pods that should be started at deployment of Bitbucket. |
| serviceAccount.clusterRole.create | bool | `true` | true if a ClusterRole should be created, or false if it already exists |
| serviceAccount.clusterRole.name | string | `nil` | Specifies the name of the ClusterRole that will be created if the "serviceAccount.clusterRole.create" flag is set. If not specified, a name will be auto-generated. |
| serviceAccount.clusterRoleBinding.create | bool | `true` | true if a ClusterRoleBinding should be created, or false if it already exists |
| serviceAccount.clusterRoleBinding.name | string | `nil` | Specifies the name of the ClusterRoleBinding that will be created if the "serviceAccount.clusterRoleBinding.create" flag is set If not specified, a name will be auto-generated. |
| serviceAccount.create | bool | `true` | true if a ServiceAccount should be created, or false if it already exists |
| serviceAccount.imagePullSecrets | list | `[]` | The list of image pull secrets that should be added to the created ServiceAccount |
| serviceAccount.name | string | `nil` | Specifies the name of the ServiceAccount to be used by the pods. If not specified, but the "serviceAccount.create" flag is set, then the ServiceAccount name will be auto-generated, otherwise the 'default' ServiceAccount will be used. |
| tolerations | list | `[]` | Standard Kubernetes tolerations that will be applied to all Bitbucket pods |
| volumes.additional | list | `[]` | Defines additional volumes that should be applied to all Bitbucket pods. Note that this will not create any corresponding volume mounts; those need to be defined in bitbucket.additionalVolumeMounts |
| volumes.localHome.customVolume | object | `{}` | When persistentVolumeClaim.create is false, then this value can be used to define a standard Kubernetes volume, which will be used for the local-home volumes. If not defined, then defaults to an emptyDir volume. |
| volumes.localHome.mountPath | string | `"/var/atlassian/application-data/bitbucket"` |  |
| volumes.localHome.persistentVolumeClaim.create | bool | `false` | If true, then a PersistentVolumeClaim will be created for each local-home volume. |
| volumes.localHome.persistentVolumeClaim.resources | object | `{"requests":{"storage":"1Gi"}}` | Specifies the standard Kubernetes resource requests and/or limits for the local-home volume claims. |
| volumes.localHome.persistentVolumeClaim.storageClassName | string | `nil` | Specifies the name of the storage class that should be used for the local-home volume claim. |
| volumes.sharedHome.customVolume | object | `{}` | When persistentVolumeClaim.create is false, then this value can be used to define a standard Kubernetes volume, which will be used for the shared-home volume. If not defined, then defaults to an emptyDir (i.e. unshared) volume. |
| volumes.sharedHome.mountPath | string | `"/var/atlassian/application-data/shared-home"` | Specifies the path in the Bitbucket container to which the shared-home volume will be mounted. |
| volumes.sharedHome.nfsPermissionFixer.command | string | `nil` | By default, the fixer will change the group ownership of the volume's root directory to match the Bitbucket container's GID (2003), and then ensures the directory is group-writeable. If this is not the desired behaviour, command used can be specified here. |
| volumes.sharedHome.nfsPermissionFixer.enabled | bool | `false` | If enabled, this will alter the shared-home volume's root directory so that Bitbucket can write to it. This is a workaround for a Kubernetes bug affecting NFS volumes: https://github.com/kubernetes/examples/issues/260 |
| volumes.sharedHome.nfsPermissionFixer.mountPath | string | `"/shared-home"` | The path in the initContainer where the shared-home volume will be mounted |
| volumes.sharedHome.persistentVolume.create | bool | `false` | If true, then a PersistentVolume will be created for the shared-home volume. |
| volumes.sharedHome.persistentVolume.mountOptions | list | `[]` | Addtional options used when mounting the volume |
| volumes.sharedHome.persistentVolume.nfs.path | string | `""` | Specifies the path exported by the NFS server, used in the mount command |
| volumes.sharedHome.persistentVolume.nfs.server | string | `""` | The address of the NFS server. It needs to be resolveable by the kubelet, so consider using an IP address. |
| volumes.sharedHome.persistentVolumeClaim.create | bool | `false` | If true, then a PersistentVolumeClaim will be created for the shared-home volume. |
| volumes.sharedHome.persistentVolumeClaim.resources | object | `{"requests":{"storage":"1Gi"}}` | Specifies the standard Kubernetes resource requests and/or limits for the shared-home volume claims. |
| volumes.sharedHome.persistentVolumeClaim.storageClassName | string | `nil` | Specifies the name of the storage class that should be used for the shared-home volume claim. |
| volumes.sharedHome.persistentVolumeClaim.volumeName | string | `nil` | Specifies the name of the persistent volume to claim |
| volumes.sharedHome.subPath | string | `nil` | Specifies the sub-directory of the shared-home volume that will be mounted in to the Bitbucket container. |

----------------------------------------------
Autogenerated from chart metadata using [helm-docs v1.5.0](https://github.com/norwoodj/helm-docs/releases/v1.5.0)
