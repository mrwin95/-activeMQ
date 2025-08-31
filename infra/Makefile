# Default project
PROJECT_NAME := infra
POSTGRES_VOLUME := $(PROJECT_NAME)_postgres-data

# bring up
up-dev:
	@echo "Stopping old container..."
	docker compose -f infra/docker-compose.yml -f infra/docker-compose.override.yml down -v
	@echo "Removing old Postgres volume ($(POSTGRES_VOLUME))..."
	-docker volume rm $(POSTGRES_VOLUME) || true
	@echo "Starting fresh stack with schema.sql preloaded..."
	docker compose -f infra/docker-compose.yml -f infra/docker-compose.override.yml up --build
# tail logs
logs:
	@echo "Tailing logs for order-service and notification-service..."
	docker compose -f infra/docker-compose.yml -f infra/docker-compose.override.yml logs -f order-service notification-server

# reset db
reset-db:
	@echo "Removing old Postgres volume ($(POSTGRES_VOLUME))..."
	-docker volume rm $(POSTGRES_VOLUME) || true
	@echo "Postgres volume cleared. Restart stack to re-init schema."

# stop container
stop:
	@echo "Stopping containers (volumes preserved)..."
	docker compose -f infra/docker-compose.yml -f infra/docker-compose.override.yml down
