<template>
<!-- todo:有时间lsn再修改一下样式 -->
  <el-form ref="form" :model="form" label-width="80px" v-if="!status">
    <el-form-item label="username">
      <el-input v-model="form.username"></el-input>
    </el-form-item>
     <el-form-item label="password">
      <el-input v-model="form.password1"></el-input>
    </el-form-item>

    <el-form-item>
      <el-button type="primary" size="medium" round @click="login">     Login     </el-button>
      <el-button type="warning" size="medium" round  @click="register">Register</el-button>
    </el-form-item>
  </el-form>

  <el-form ref="form" :model="form" label-width="130px" v-else>
    <el-form-item label="username">
      <el-input v-model="form.username"></el-input>
    </el-form-item>
     <el-form-item label="set password">
      <el-input v-model="form.password1"></el-input>
    </el-form-item>
    <el-form-item label="confirm password">
      <el-input v-model="form.password2"></el-input>
    </el-form-item>
    <el-form-item label="email">
      <el-input v-model="form.email"></el-input>
    </el-form-item>
    <el-form-item label="identify code">
      <el-button type="info" @click="sendEmail" icon="el-icon-message" circle></el-button>click to send identify code
      <el-input v-model="form.identify_code" aria-placeholder="input your code from this email"></el-input>
    </el-form-item>

    <el-form-item>
      <el-button type="primary" size="medium" round @click="login">     Login     </el-button>
      <el-button type="warning" size="medium" round  @click="register">Register</el-button>
    </el-form-item>

  </el-form>
</template>

<script>
  export default {
    data() {
      return {
        //status表示当前位于哪个标签卡，0表示登录，1表示注册
        status:0,
        form: {
         username:"",
         password1:"",
         password2:"",
         email:"",
         identify_code:""

        }
      }
    },
    methods: {
     //todo:添加login和register方法。
     login(){
       if (this.status === 0) {
         const data = new FormData()
         data.append('username', this.form.username)
         data.append('password', this.form.password1)
         this.request.post('http://101.42.173.97:8090/login', data).then(res => {
           console.log('res', res)
           if (res.data.success === true) {
             this.$cookies.set("oatoken",res.data.jwt,'1h')
             this.$cookies.set("userId",res.data.user_id,'1h')
             console.log(this.$cookies.get("oatoken"))
             this.$router.push('/tools')
           }
           // this.$cookies.set("oatoken",res.data.data.token,'7d')
           else {
             console.log('登录失败')
             this.$message({
               message: res.data.message,
               type: 'warning'
             });
           }
         })
       } else {
         this.status = 0;
       }

      //如果当前未登录且在登录标签卡，点击后验证用户名和密码（成功则跳转到tools页面）
      //如果当前未登录且在注册标签卡，点击后跳转到登录标签卡(转换status)
     },
     register(){
       if (this.status === 1) {
         const data = new FormData()
         data.append('email', this.form.email)
         data.append('identify_code', this.form.identify_code)
         data.append('username', this.form.username)
         data.append('password1', this.form.password1)
         data.append('password2', this.form.password2)
         this.request.post('http://101.42.173.97:8090/register/check_identify_code', data).then(res => {
           console.log('res', res)
           if (res.data.success === true) {
             this.$message({
               message: res.data.message,
               type: 'success'
             });
             this.status = 0
           }
           // this.$cookies.set("oatoken",res.data.data.token,'7d')
           else {
             this.$message({
               message: res.data.message,
               type: 'warning'
             });
           }
         })
       } else {
         this.status = 1;
       }
      //如果当前未登录且在注册标签卡，点击后提交注册信息（成功则跳转到tools页面）
      //如果当前未登录且在登录标签卡，点击后跳转到注册标签卡（转换status）
     },
     sendEmail(){
       const data = new FormData()
       data.append('email', this.form.email)
       this.request.post('http://101.42.173.97:8090/register/identify_code_send', data).then(res => {
         console.log('res', res)
         if (res.data.success === true) {
           this.$message({
             message: res.data.message,
             type: 'success'
           });
         }
         // this.$cookies.set("oatoken",res.data.data.token,'7d')
         else {
           this.$message({
             message: res.data.message,
             type: 'warning'
           });
         }
       })
     }
    }
  }
</script>
