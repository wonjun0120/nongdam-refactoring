output "vpc_id" {
  description = "ID of the created VPC"
  value       = module.vpc.vpc_id
}

output "ec2_instance_id" {
  description = "ID of the created EC2 instance"
  value       = module.ec2.instance_id
}
