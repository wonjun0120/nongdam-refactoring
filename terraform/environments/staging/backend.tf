terraform {
  backend "s3" {
    bucket         = "nebula-terraform-state-seoul"
    key            = "staging/terraform.tfstate"
    region         = "ap-northeast-2"
    dynamodb_table = "terraform-lock"
  }
}
