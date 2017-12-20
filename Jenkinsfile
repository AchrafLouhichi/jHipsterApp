#!groovy

stage 'Dev'
node {
    checkout scm
    mvn ' -Dmaven.test.skip=true'
    
}

stage 'QA'
parallel(longerTests: {
    runTests(30)
}, quickerTests: {
    runTests(20)
})


stage name: 'Production', concurrency: 1

def mvn(args) {
    sh "mvn ${args}"
}

def runTests(duration) {
    node {
        sh "sleep ${duration}"
        }
    }

def deploy(id) {

}

def undeploy(id) {
    
}
