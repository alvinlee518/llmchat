#!/bin/bash

set -e # 遇到错误立即退出
set -o pipefail # 管道命令中某个命令失败时立即退出

# 定义工作目录和其他变量
WORK_DIR=$(pwd)
ENV_FILE="${WORK_DIR}/docker/.env"
ENV_EXAMPLE_FILE="${WORK_DIR}/docker/.env.example"
SERVER_DOCKERFILE="${WORK_DIR}/docker/dockerfile-server"
UI_DOCKERFILE="${WORK_DIR}/docker/dockerfile-ui"
COMPOSE_FILE="${WORK_DIR}/docker/docker-compose.yml"
UI_DIR="${WORK_DIR}/../llmchat-ui"
POM_FILE="${WORK_DIR}/../pom.xml"

echo ">>> [INFO] 当前工作目录：${WORK_DIR}"

# 1. 复制 .env 文件
if [[ -f "${ENV_EXAMPLE_FILE}" ]]; then
  echo ">>> [INFO] 复制 .env 文件"
  cp "${ENV_EXAMPLE_FILE}" "${ENV_FILE}"
else
  echo ">>> [ERROR] 找不到 ${ENV_EXAMPLE_FILE}"
  exit 1
fi

# 2. 构建后端 Maven 项目
if [[ -f "${POM_FILE}" ]]; then
  echo ">>> [INFO] 构建后端项目（跳过测试）"
  mvn -Dmaven.test.skip=true clean package -U -Pprod -f "${POM_FILE}"
else
  echo ">>> [ERROR] 找不到 ${POM_FILE}"
  exit 1
fi

# 3. 构建前端项目
if [[ -d "${UI_DIR}" ]]; then
  echo ">>> [INFO] 进入前端目录并安装依赖"
  cd "${UI_DIR}"
  pnpm install && pnpm run build
  cd "${WORK_DIR}"
else
  echo ">>> [ERROR] 找不到前端目录：${UI_DIR}"
  exit 1
fi

# 4. 构建后端 Docker 镜像
if [[ -f "${SERVER_DOCKERFILE}" ]]; then
  echo ">>> [INFO] 构建后端 Docker 镜像"
  docker build -t llmchat/llmchat-server:latest -f "${SERVER_DOCKERFILE}" ../
else
  echo ">>> [ERROR] 找不到后端 Dockerfile：${SERVER_DOCKERFILE}"
  exit 1
fi

# 5. 构建前端 Docker 镜像
if [[ -f "${UI_DOCKERFILE}" ]]; then
  echo ">>> [INFO] 构建前端 Docker 镜像"
  docker build -t llmchat/llmchat-ui:latest -f "${UI_DOCKERFILE}" ../
else
  echo ">>> [ERROR] 找不到前端 Dockerfile：${UI_DOCKERFILE}"
  exit 1
fi

# 6. 启动 Docker Compose 服务
if [[ -f "${COMPOSE_FILE}" ]]; then
  echo ">>> [INFO] 启动 Docker Compose 服务"
  docker-compose -p llmchat -f "${COMPOSE_FILE}" up -d
else
  echo ">>> [ERROR] 找不到 Docker Compose 文件：${COMPOSE_FILE}"
  exit 1
fi

echo ">>> [INFO] 脚本执行完毕，服务已启动"
