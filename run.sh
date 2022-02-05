DOCKER_COMPOSE=$(groups | grep -q "docker" && echo "docker-compose" || echo "sudo docker-compose")

${DOCKER_COMPOSE} kill -s SIGKILL
${DOCKER_COMPOSE} rm -v --force
${DOCKER_COMPOSE} pull || ${DOCKER_COMPOSE} pull || ${DOCKER_COMPOSE} pull # pull sometimes fails, retrying helps
${DOCKER_COMPOSE} up -d --remove-orphans redis