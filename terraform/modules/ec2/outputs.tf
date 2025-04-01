output "instance_id" {
  description = "생성된 EC2 인스턴스의 ID"
  value       = aws_instance.this.id
}

output "elastic_ip" {
  description = "EC2 인스턴스에 할당된 탄력적(EIP) IP 주소"
  value       = aws_eip.this.public_ip
}
