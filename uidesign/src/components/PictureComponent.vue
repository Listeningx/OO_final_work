<template>
<div id="root">
    <el-form ref="form" :model="form" label-width="80px" :inline="true">
      <el-form-item label="文本内容" class="item" line-height="500px">
          <el-input type="textarea" v-model="form.text" style="width: 450px;"></el-input>
      </el-form-item>
      <el-form-item label="标题" class="item" inline="true">
          <el-input v-model="form.title" style="width: 225px;"></el-input>
      </el-form-item>
      <el-form-item label="颜色" class="item" inline="true">
          <!-- <ColorPicker id="cp"  v-model="form.color" style="width: 450px;display: inline-block;"></ColorPicker>
          <el-tag type="success">{{form.color}}</el-tag> -->
          <el-color-picker v-model="form.color"></el-color-picker>
      </el-form-item>
      <el-form-item label="字体" class="item">
        <el-radio-group v-model="form.font">
          <el-radio label="0"><img src="../assets/fonts/潦草0.png" style="width: 180px;" alt="潦草字体"></el-radio>
          <el-radio label="1"><img src="../assets/fonts/青春1.png" style="width: 180px;" alt="青春字体"></el-radio>
          <el-radio label="2"><img src="../assets/fonts/浪漫2.png" style="width: 180px;" alt="浪漫字体"></el-radio>
          <el-radio label="3"><img src="../assets/fonts/淘气3.png" style="width: 180px;" alt="淘气字体"></el-radio>
          <el-radio label="4"><img src="../assets/fonts/梅花4.png" style="width: 180px;" alt="梅花字体"></el-radio>
          <el-radio label="5"><img src="../assets/fonts/潇洒5.png" style="width: 180px;" alt="潇洒字体"></el-radio>
          <el-radio label="6"><img src="../assets/fonts/鸿鹄6.png" style="width: 180px;" alt="鹄字体"></el-radio>
        </el-radio-group>
      </el-form-item>
      <el-form-item label="背景" class="item">
      <el-radio-group v-model="form.background">
        <el-radio label="0"><img src="../assets/信纸/红线.png" style="width: 80px;" alt="红线"></el-radio>
        <el-radio label="1"><img src="../assets/信纸/北航.png" style="width: 80px;" alt="北航"></el-radio>
        <el-radio label="2"><img src="../assets/信纸/东北大学.jpg" style="width: 80px;" alt="东大"></el-radio>
        <el-radio label="3"><img src="../assets/信纸/白纸.jpg" style="width: 80px;" alt="东大"></el-radio>
        <el-radio label="4"><img src="../assets/信纸/透明.jpg" style="width: 80px;" alt="东大"></el-radio>

      </el-radio-group>
    </el-form-item>
     
      <el-form-item>
          <el-button type="success" @click="onSubmit" style="position:relative;left:300px">立即生成</el-button>
          <!-- <el-button>取消</el-button> -->
      </el-form-item>
    </el-form>
</div>
</template>

<script>
// import ColorPicker from "../components/color-picker.vue"
// import ToolsView from "@/views/ToolsView.vue";


  export default {
    data() {
      return {
        form: {
          title: '',
          user_id: '',
          color: 'black',
          font:'',
          background:'',
          text: '',
          jwt:''
        }
      }
    },

    methods: {
     onSubmit() {
        console.log(this.form);
        
          console.log('txt2hand');
        }
      
    },
    components:{
        // ColorPicker
    }
  }
</script>

<style>
#root{
    fill: rgb(49, 67, 46);
}
.item .el-form-item__label{
    color: rgb(66, 39, 87);
    font-size: medium;
    font-weight: 800;
    font-family: 'PingFang SC';
  }

  .el-textarea__inner{
    height: 200px;
  }
</style>