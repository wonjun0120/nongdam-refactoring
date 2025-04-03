variable "aws_region" {
  description = "AWS region for staging environment"
  type        = string
  default     = "ap-northeast-2"
}

variable "vpc_cidr" {
  description = "CIDR block for the VPC in staging"
  type        = string
  default     = "10.0.0.0/16"
}

variable "instance_type" {
  description = "EC2 instance type for staging environment"
  type        = string
  default     = "t2.micro"
}
