node {
  stage 'Build'
  checkout scm
  sh 'mvn clean package -U'
}

parallel 'quality scan': {
  node {
    sh 'mvn sonar:sonar'
  }
}, 'integration test': {
    node {
        sh 'mvn verify'
    }
}