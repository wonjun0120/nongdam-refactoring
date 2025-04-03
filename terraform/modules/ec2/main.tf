# 최신 Ubuntu 20.04 LTS (Focal Fossa) AMI 조회
data "aws_ami" "ubuntu" {
  most_recent = true
  owners      = ["099720109477"]

  filter {
    name   = "name"
    values = ["ubuntu/images/hvm-ssd/ubuntu-focal-20.04-amd64-server-*"]
  }

  filter {
    name   = "virtualization-type"
    values = ["hvm"]
  }
}

# 보안 그룹 생성: SSH (22)와 애플리케이션 포트 (8080) 개방
resource "aws_security_group" "this" {
  name        = "${var.env}-ec2-sg"
  description = "Security group for ${var.env} EC2 instance"
  vpc_id      = var.vpc_id

  ingress {
    description = "SSH access"
    from_port   = 22
    to_port     = 22
    protocol    = "tcp"
    cidr_blocks = var.allowed_ssh_cidrs
  }

  ingress {
    description = "Application access on port 8080"
    from_port   = 8080
    to_port     = 8080
    protocol    = "tcp"
    cidr_blocks = var.allowed_app_cidrs
  }

  egress {
    description = "Allow all outbound traffic"
    from_port   = 0
    to_port     = 0
    protocol    = "-1"
    cidr_blocks = ["0.0.0.0/0"]
  }

  tags = {
    Name        = "${var.env}-ec2-sg"
    Environment = var.env
  }
}

# EC2 인스턴스 생성 (기존 PEM 키 사용, 탄력적 IP는 별도 리소스에서 할당)
resource "aws_instance" "this" {
  ami                         = data.aws_ami.ubuntu.id
  instance_type               = var.instance_type
  subnet_id                   = var.subnet_id
  key_name                    = var.key_name
  associate_public_ip_address = false

  vpc_security_group_ids = [aws_security_group.this.id]

  tags = {
    Name        = "${var.env}-test-instance"
    Environment = var.env
  }

  # user_data 추가: 파일로부터 읽어온 내용을 전달
  user_data = var.user_data
}

# 탄력적 IP(EIP)를 생성하여 EC2 인스턴스에 할당
resource "aws_eip" "this" {
  instance = aws_instance.this.id
  domain   = "vpc"

  tags = {
    Name        = "${var.env}-ec2-eip"
    Environment = var.env
  }
}
