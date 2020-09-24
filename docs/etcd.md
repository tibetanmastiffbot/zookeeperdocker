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

set the CA, CERT and PUB KEY files

    export MYCACERT=/etc/kubernetes/pki/etcd-manager/etcd-clients-ca.crt
    export MYCERT=$MYCACERT
    export MYKEY=/etc/kubernetes/pki/etcd-manager/etcd-clients-ca.key

List keys prefixed with `/registry/masterleases`

    /opt/etcd-v3.4.3-linux-amd64/etcdctl \
      --endpoints=https://127.0.0.1:4001 \
      --cacert=${MYCACERT} --cert=${MYCERT} --key=${MYKEY} \
      get --prefix --keys-only /registry/masterleases

List all keys

    /opt/etcd-v3.4.3-linux-amd64/etcdctl \
      --endpoints=https://127.0.0.1:4001 \
      --cacert=${MYCACERT} --cert=${MYCERT} --key=${MYKEY} \
      get / --prefix --keys-only


