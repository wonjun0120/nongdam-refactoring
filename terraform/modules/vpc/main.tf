# VPC 생성
resource "aws_vpc" "this" {
  cidr_block           = var.vpc_cidr
  enable_dns_support   = true
  enable_dns_hostnames = true

  tags = {
    Name        = "${var.env}-vpc"
    Environment = var.env
  }
}

# 인터넷 게이트웨이 생성
resource "aws_internet_gateway" "this" {
  vpc_id = aws_vpc.this.id

  tags = {
    Name        = "${var.env}-igw"
    Environment = var.env
  }
}

# 퍼블릭 서브넷 생성 (첫 번째 가용 영역 사용)
resource "aws_subnet" "public" {
  vpc_id                  = aws_vpc.this.id
  cidr_block              = element(var.public_subnet_cidrs, 0)
  availability_zone       = element(var.azs, 0)
  map_public_ip_on_launch = true

  tags = {
    Name        = "${var.env}-public-subnet"
    Environment = var.env
  }
}

# 퍼블릭 라우트 테이블 생성
resource "aws_route_table" "public" {
  vpc_id = aws_vpc.this.id

  tags = {
    Name        = "${var.env}-public-rt"
    Environment = var.env
  }
}

# 인터넷 접근을 위한 라우트 추가 (0.0.0.0/0)
resource "aws_route" "public_internet_access" {
  route_table_id         = aws_route_table.public.id
  destination_cidr_block = "0.0.0.0/0"
  gateway_id             = aws_internet_gateway.this.id
}

# 퍼블릭 서브넷에 라우트 테이블 연결
resource "aws_route_table_association" "public" {
  subnet_id      = aws_subnet.public.id
  route_table_id = aws_route_table.public.id
}
