pipeline {
    agent any
    
    environment {
        JAVA_HOME = 'E:\\AAA_CSWork\\JDK'
        MAVEN_HOME = 'E:\\AAA_CSWork\\maven\\apache-maven-3.8.5'
    }
    
    stages {
        stage('Checkout') {
            steps {
                checkout scm
            }
        }
        stage('Compile') {
            steps {
                bat "mvn compile"
            }
        }
        stage('Package') {
            steps {
                bat "mvn --% clean package -Dmaven.test.skip=true"
            }
        }
        stage('Deploy') {
            steps {
                bat '''
                    taskkill /f /fi "WINDOWTITLE eq *bank-liability*" 2>nul
                    taskkill /f /fi "WINDOWTITLE eq *bank-customer*" 2>nul
                    taskkill /f /fi "WINDOWTITLE eq *node*" 2>nul
                    timeout /t 3
                    start /B java -jar bank-core\\bank-liability\\target\\bank-liability-1.0.0-SNAPSHOT.jar
                    start /B java -jar bank-core\\bank-customer\\target\\bank-customer-1.0.0-SNAPSHOT.jar
                    cd bank-web
                    start /B node node_modules\\.bin\\vite --port 3000
                '''
            }
        }
        stage('Health Check') {
            steps {
                script {
                    def ok = true
                    try {
                        bat "curl.exe -s -o NUL -w %%{http_code} http://localhost:8080/api/liability/product"
                    } catch(Exception e) { ok = false }
                    try {
                        bat "curl.exe -s -o NUL -w %%{http_code} http://localhost:3000"
                    } catch(Exception e) { ok = false }
                    if (!ok) error('Health check failed')
                }
            }
        }
    }
    
    post {
        failure {
            echo '=== PIPELINE FAILED ==='
        }
        success {
            echo '=== PIPELINE PASSED ==='
        }
    }
}
