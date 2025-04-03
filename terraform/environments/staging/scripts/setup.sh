#!/bin/bash
set -eux

echo "setup.sh 실행 시작" | sudo tee -a /var/log/setup.log

DEVICE=$(lsblk -o NAME,SIZE | grep 30G | awk '{print "/dev/"$1}' | head -n 1)

if [ -n "$DEVICE" ]; then
    sudo mkfs -t ext4 "$DEVICE"
    sudo mkdir -p /mnt/extra
    sudo mount "$DEVICE" /mnt/extra
    echo "$DEVICE /mnt/extra ext4 defaults,nofail 0 2" | sudo tee -a /etc/fstab
    sudo chmod 777 /mnt/extra
    echo "✅ 추가 볼륨 마운트 완료: $DEVICE" | sudo tee -a /var/log/setup.log
else
    echo "❌ 추가 EBS 볼륨을 찾을 수 없음" | sudo tee -a /var/log/setup.log
fi

echo "Docker 설치 시작" | sudo tee -a /var/log/setup.log
sudo apt-get update -y
sudo apt-get upgrade -y
sudo apt-get install -y apt-transport-https ca-certificates curl software-properties-common

curl -fsSL https://download.docker.com/linux/ubuntu/gpg | sudo apt-key add -
sudo add-apt-repository -y "deb [arch=amd64] https://download.docker.com/linux/ubuntu $(lsb_release -cs) stable"
sudo apt-get update -y
sudo apt-get install -y docker-ce docker-ce-cli containerd.io docker-compose-plugin

sudo systemctl enable docker
sudo systemctl start docker
sudo usermod -aG docker ubuntu
# newgrp 명령은 자동화 환경에서는 사용하지 않습니다.
# (그룹 변경 사항은 재로그인 후에 반영되므로, 필요 시 인스턴스 재접속)

echo "Docker 및 Docker Compose 설치 완료" | sudo tee -a /var/log/setup.log
echo "setup.sh 실행 완료" | sudo tee -a /var/log/setup.log
