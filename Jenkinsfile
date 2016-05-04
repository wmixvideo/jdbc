node {
  stage 'Clean'
  checkout scm
  sh 'mvn clean'
}
node {
  stage 'Package'
  checkout scm
  sh 'mvn package -U'
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