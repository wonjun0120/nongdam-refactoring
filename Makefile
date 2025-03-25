# 환경변수 자동 로딩
include .env.local
export

# Docker Compose 명령어들
up:
	docker-compose up --build -d

down:
	docker-compose down

restart:
	docker-compose down && docker-compose up --build -d

logs:
	docker-compose logs -f app

bash:
	docker-compose exec app /bin/bash

# DB 접속
mysql:
	docker-compose exec mysql mysql -u$(SPRING_DATASOURCE_USERNAME) -p$(SPRING_DATASOURCE_PASSWORD) $(MYSQL_DATABASE)

# MinIO 접속
minio:
	docker-compose exec minio sh

# Spring Boot 명령어
build:
	./gradlew clean build

test:
	./gradlew test

querydsl:
	./gradlew clean build -x test

# 운영 배포
prod:
	docker-compose -f docker-compose.yml -f docker-compose.prod.yml up -d

# Docker 정리
prune:
	docker system prune -f

# 헬프
help:
	@echo "🛠️  사용 가능한 명령어:"
	@echo "  make up         - 로컬 개발 서버 실행"
	@echo "  make down       - 컨테이너 종료"
	@echo "  make restart    - 재시작"
	@echo "  make logs       - 서버 로그 보기"
	@echo "  make bash       - 앱 컨테이너 쉘 접속"
	@echo "  make mysql      - MySQL 접속"
	@echo "  make minio      - MinIO 접속"
	@echo "  make build      - 프로젝트 빌드"
	@echo "  make test       - 테스트 실행"
	@echo "  make querydsl   - Q 클래스 생성"
	@echo "  make prod       - 운영 배포 실행"
	@echo "  make prune      - 도커 정리"

# PHONY 선언
.PHONY: up down restart logs bash mysql minio build test querydsl prod prune help
