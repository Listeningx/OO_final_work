<template>
  <el-form ref="form" :model="form" label-width="80px">
    <el-form-item>
      <el-button type="primary" size="medium" round @click="login"> {{ data }}     </el-button>
    </el-form-item>
  </el-form>
</template>

<script>
function sleep(numberMillis) {
  var now = new Date();
  var exitTime = now.getTime() + numberMillis;
  while (now.getTime() < exitTime) {
    now = new Date();
  }
}
export default {
  data() {
    return {
      data:0.0
    }
  },
  methods: {
    async login() {
      const data = new FormData()
      data.append('title', 'aa')
      data.append('text', 'aassssssssssssasdasdasdasdsadasdasdasdasdssssssssssssssssssssssssssssssssssssssssssssssssssssssss')
      data.append('color', '#000000')
      data.append('user_id', 10)
      data.append('jwt', 'eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJqdGkiOiIxMCIsImV4cCI6MTY1NTIyMDk3Nywic3ViIjoiZngyIiwiaWF0IjoxNjU1MjE5MTc3fQ.v8BdAiN-20rdqn1U5QjnL6P2dpgyTSIlelRV72GNhlA')
      this.request.post('http://101.42.173.97:8090/text', data).then(async res => {
        console.log('res', res)
        if (res.data.success === true) {
          console.log('trueaa')
        } else {
          this.$message({
            message: res.data.message,
            type: 'warning'
          });
        }
      })
      var i = 1.0;
      while (i < 100.0) {
        if (i === 1.0) {
          sleep(5000)    // 1s
        }
        console.log(i)
        console.log(i < 20.0)
        console.log('a')
        sleep(1000)    // 1s
        const data2 = new FormData()
        data2.append('user_id', 10)
        await this.request.post('http://101.42.173.97:8090/get_img_to_txt_process', data2).then(res => {
          console.log(res.data.process)
          console.log('b')
          this.data = res.data.process
          i = res.data.process
        })
      }
    }
  }
}
</script>
