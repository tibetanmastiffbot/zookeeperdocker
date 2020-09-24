# Installation

https://github.com/etcd-io/etcd/releases

    ETCD_VER=v3.4.13

    # choose either URL
    GOOGLE_URL=https://storage.googleapis.com/etcd
    GITHUB_URL=https://github.com/etcd-io/etcd/releases/download
    DOWNLOAD_URL=${GOOGLE_URL}

    rm -f /tmp/etcd-${ETCD_VER}-linux-amd64.tar.gz
    rm -rf /tmp/etcd-download-test && mkdir -p /tmp/etcd-download-test

    curl -L ${DOWNLOAD_URL}/${ETCD_VER}/etcd-${ETCD_VER}-linux-amd64.tar.gz -o /tmp/etcd-${ETCD_VER}-linux-amd64.tar.gz
    tar xzvf /tmp/etcd-${ETCD_VER}-linux-amd64.tar.gz -C /tmp/etcd-download-test --strip-components=1
    rm -f /tmp/etcd-${ETCD_VER}-linux-amd64.tar.gz

    /tmp/etcd-download-test/etcd --version
    /tmp/etcd-download-test/etcdctl version

# Access

On the K8S API Server,

    sudo curl https://localhost:4001/metrics -k \
      --cert /etc/kubernetes/pki/etcd-manager-main/etcd-clients-ca.crt \
      --key /etc/kubernetes/pki/etcd-manager-main/etcd-clients-ca.key

Copy the tar.gz into etcd manager, extract it under /tmp/1

    /opt/etcd-v3.4.3-linux-amd64/etcdctl \
      --endpoints=https://127.0.0.1:4001 \
      --cacert=/etc/kubernetes/pki/etcd-manager/etcd-clients-ca.crt \
      --cert=/etc/kubernetes/pki/etcd-manager/etcd-clients-ca.crt \
      --key=/etc/kubernetes/pki/etcd-manager/etcd-clients-ca.key \
      get --prefix --keys-only /registry/masterleases

List all keys

    export ETCDCTL_API=3
    /opt/etcd-v3.4.3-linux-amd64/etcdctl \
      --endpoints=https://127.0.0.1:4001 \
      --cacert=/etc/kubernetes/pki/etcd-manager/etcd-clients-ca.crt \
      --cert=/etc/kubernetes/pki/etcd-manager/etcd-clients-ca.crt \
      --key=/etc/kubernetes/pki/etcd-manager/etcd-clients-ca.key \
      get / --prefix --keys-only


