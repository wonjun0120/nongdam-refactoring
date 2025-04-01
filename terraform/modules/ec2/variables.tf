variable "instance_type" {
  description = "EC2 인스턴스의 타입"
  type        = string
}

variable "subnet_id" {
  description = "EC2 인스턴스가 배포될 서브넷 ID"
  type        = string
}

variable "env" {
  description = "배포 환경 (dev, staging, prod 등)"
  type        = string
}

variable "vpc_id" {
  description = "EC2 인스턴스가 속할 VPC의 ID"
  type        = string
}

variable "key_name" {
  description = "사용할 PEM 키의 이름 (이미 존재하는 키)"
  type        = string
}

variable "allowed_ssh_cidrs" {
  description = "SSH 접속을 허용할 CIDR 리스트"
  type        = list(string)
  default     = ["0.0.0.0/0"]
}

variable "allowed_app_cidrs" {
  description = "8080 포트 접속을 허용할 CIDR 리스트"
  type        = list(string)
  default     = ["0.0.0.0/0"]
}
