provider "aws" {
  region = var.aws_region
}

# VPC 모듈 호출 (modules/vpc 폴더에 모듈 작성)
module "vpc" {
  source   = "../../modules/vpc"
  cidr     = var.vpc_cidr
  env      = "staging"
}

# EC2 모듈 호출 (modules/ec2 폴더에 모듈 작성)
module "ec2" {
  source         = "../../modules/ec2"
  instance_type  = var.instance_type
  key_name       = "ec2-nongdam.pem"
  vpc_id         = module.vpc.vpc_id
  subnet_id      = module.vpc.public_subnet_ids[0]  # 예시로 첫 번째 퍼블릭 서브넷 선택
  env            = "staging"
}
