# Default project
PROJECT_NAME := infra
POSTGRES_VOLUME := postgresql_postgres_data

# bring up
up-dev:
	@echo "Stopping old container..."
	docker compose -f infra/docker-compose.yml -f infra/docker-compose-override.yml down -v
	@echo "Removing old Postgres volume ($(POSTGRES_VOLUME))..."
	-docker volume rm $(POSTGRES_VOLUME) || true
	@echo "Starting fresh stack with schema.sql preloaded..."
	docker compose -f infra/docker-compose.yml -f infra/docker-compose-override.yml up --build
# tail logs
logs:
	@echo "Tailing logs for order-service and notification-service..."
	docker compose -f infra/docker-compose.yml -f infra/docker-compose-override.yml logs -f order-service notification-service

# reset db
reset-db:
	@echo "Removing old Postgres volume ($(POSTGRES_VOLUME))..."
	-docker volume rm $(POSTGRES_VOLUME) || true
	@echo "Postgres volume cleared. Restart stack to re-init schema."

# stop
stop:
	@echo "Stopping containers (volumes preserved)..."
	docker compose -f infra/docker-compose.yml -f infra/docker-compose-override.yml down

#list running services
ps:
	@echo "Listing running containers..."
	docker compose -f infra/docker-compose.yml -f infra/docker-compose-override.yml ps
