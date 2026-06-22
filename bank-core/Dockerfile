# ===== Stage 1: Build with Maven =====
FROM maven:3.8.5-eclipse-temurin-17 AS build
WORKDIR /build

# 复制 pom 文件（利用 Docker 缓存层，不改 pom 就不重下载依赖）
COPY pom.xml ./
COPY bank-common/pom.xml bank-common/
COPY bank-teller/pom.xml bank-teller/
COPY bank-customer/pom.xml bank-customer/
COPY bank-liability/pom.xml bank-liability/
COPY bank-server/pom.xml bank-server/

# 下载依赖（允许失败，后续正式编译会补全）
RUN mvn dependency:go-offline -pl bank-server -am -B || true

# 复制全部源码
COPY bank-common bank-common/
COPY bank-teller bank-teller/
COPY bank-customer bank-customer/
COPY bank-liability bank-liability/
COPY bank-server bank-server/

# 编译打包
RUN mvn package -pl bank-server -am -DskipTests -B

# ===== Stage 2: Runtime with JRE =====
FROM eclipse-temurin:17-jre
WORKDIR /app
COPY --from=build /build/bank-server/target/bank-server-*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]