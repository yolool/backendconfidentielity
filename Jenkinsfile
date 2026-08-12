pipeline {
    agent any
    
    tools {
        maven 'Maven-3'
    }
    
    environment {
        DOCKER_IMAGE = 'adam020/backend-confidentiality'
        DOCKER_TAG   = "${BUILD_NUMBER}"
        TELEGRAM_CHAT_ID = '-1003962149031'
    }
    
    stages {
        
        stage('1. Checkout Code') {
            steps {
                echo '📥 Pulling code from GitHub...'
                git branch: 'master', url: 'https://github.com/yolool/backendconfidentielity.git'
            }
        }
        
        stage('2. Build & Test') {
            steps {
                echo '🔨 Building with Maven and running JUnit tests...'
                sh 'mvn clean package -DskipTests=false'
            }
        }
        
        stage('3. SonarQube Analysis') {
            steps {
                echo '🔍 Scanning code with SonarQube...'
                withSonarQubeEnv('sonarqube') {
                    sh '''
                        mvn sonar:sonar \
                        -Dsonar.projectKey=backend-confidentiality \
                        -Dsonar.projectName=Backend-Confidentiality \
                        -Dsonar.host.url=$SONAR_HOST_URL \
                        -Dsonar.token=$SONAR_AUTH_TOKEN
                    '''
                }
            }
        }
        
        stage('4. Docker Build') {
            steps {
                echo '🐳 Building Docker image...'
                sh "docker build -t ${DOCKER_IMAGE}:${DOCKER_TAG} ."
                sh "docker tag ${DOCKER_IMAGE}:${DOCKER_TAG} ${DOCKER_IMAGE}:latest"
            }
        }
        
        stage('5. Docker Push') {
            steps {
                echo '🚀 Pushing image to Docker Hub...'
                withCredentials([usernamePassword(
                    credentialsId: 'docker-hub-creds',
                    usernameVariable: 'DOCKER_USER',
                    passwordVariable: 'DOCKER_PASS'
                )]) {
                    sh 'echo $DOCKER_PASS | docker login -u $DOCKER_USER --password-stdin'
                    sh "docker push ${DOCKER_IMAGE}:${DOCKER_TAG}"
                    sh "docker push ${DOCKER_IMAGE}:latest"
                    sh 'docker logout'
                }
            }
        }
    }
    
    post {
        always {
            cleanWs()
        }
        success {
            withCredentials([string(credentialsId: 'telegram-token', variable: 'TG_TOKEN')]) {
                sh '''
                    curl -s -X POST "https://api.telegram.org/bot${TG_TOKEN}/sendMessage" \
                      --data-urlencode "chat_id=${TELEGRAM_CHAT_ID}" \
                      --data-urlencode "text=✅ BACKEND build #${BUILD_NUMBER} SUCCESS"
                '''
            }
        }
        failure {
            withCredentials([string(credentialsId: 'telegram-token', variable: 'TG_TOKEN')]) {
                sh '''
                    curl -s -X POST "https://api.telegram.org/bot${TG_TOKEN}/sendMessage" \
                      --data-urlencode "chat_id=${TELEGRAM_CHAT_ID}" \
                      --data-urlencode "text=❌ BACKEND build #${BUILD_NUMBER} FAILED — check Jenkins!"
                '''
            }
        }
    }
}