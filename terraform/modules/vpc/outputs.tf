output "vpc_id" {
  description = "생성된 VPC의 ID"
  value       = aws_vpc.this.id
}

output "public_subnet_ids" {
  description = "생성된 퍼블릭 서브넷의 ID 목록"
  value       = [aws_subnet.public.id]
}

output "igw_id" {
  description = "생성된 인터넷 게이트웨이의 ID"
  value       = aws_internet_gateway.this.id
}
