<template>
  <div id="root">
    <PictureCard style="position:fixed;top:130px;left:5px" ref="pictureCard" v-if="status" :imges="imges"/>
    <progress-bar v-if="!status"
                  :options="options"
                  :value="proValue" style="position: fixed;top: 0px;left:0px;"
    />
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
          <el-radio label="7"><img src="../assets/fonts/english7.png" style="width: 180px;" alt="鹄字体"></el-radio>
        </el-radio-group>
      </el-form-item>
      <el-form-item label="背景" class="item">
        <el-radio-group v-model="form.background">
          <el-radio label="2"><img src="../assets/信纸/红线.png" style="width: 80px;" alt="红线"></el-radio>
          <el-radio label="0"><img src="../assets/信纸/北航.png" style="width: 80px;" alt="北航"></el-radio>
          <el-radio label="1"><img src="../assets/信纸/东北大学.jpg" style="width: 80px;" alt="东大"></el-radio>
          <el-radio label="-1"><img src="../assets/信纸/白纸.jpg" style="width: 80px;" alt="东大"></el-radio>
          <el-radio label="-2"><img src="../assets/信纸/透明.jpg" style="width: 80px;" alt="东大"></el-radio>

        </el-radio-group>
      </el-form-item>

      <el-form-item>
        <el-button type="success" @click="onSubmit" style="position:relative;left:300px">立即生成</el-button>
      </el-form-item>
    </el-form>
  </div>
</template>

<script>
import PictureCard from "../components/CarouselCard.vue"
// import ToolsView from "@/views/ToolsView.vue";

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
      pictureCard: 'http://101.42.173.97:8090/images/9/326/img0.png',
      status:1,
      form: {
        title: '',
        user_id: '',
        color: 'black',
        font:'',
        background:'',
        text: '',
        jwt:''
      },
      imges:[
      ],//储存照片墙展示的图片url（就是文本转手写返回来的图片）

      options: {
        text: {
          color: '#FFFFFF',
          shadowEnable: true,
          shadowColor: '#000000',
          fontSize: 14,
          fontFamily: 'Helvetica',
          dynamicPosition: false,
          hideText: false
        },
        progress: {
          color: '#71d0a8',
          backgroundColor: '#457341',
          inverted: false
        },
        layout: {
          height: 100,
          width: 140,
          verticalTextAlign: 61,
          horizontalTextAlign: 43,
          zeroOffset: 0,
          strokeWidth: 300,
          progressPadding: 0,
          type: 'cylinder',
          position:'absolute',
          left:'300px',
        }
      },
      proValue:0,//进度条数值
    }
  },
  mounted(){
    // this.$refs.pictureCard.imges = this.imges;
  },
  methods: {
    async onSubmit() {
      console.log(this.form);
      this.$emit("changeStatus");
      const data = new FormData()
      data.append('title', this.form.title)
      data.append('text', this.form.text)
      data.append('color', this.form.color)
      data.append('user_id', this.$cookies.get("userId"))
      data.append('jwt', this.$cookies.get("oatoken"))
      data.append('type', this.form.font)
      data.append('background_type', this.form.background)
      this.request.post('http://101.42.173.97:8090/text', data).then(async res => {
        console.log(';;;;;;;;;;;;;;;')
        console.log('res', res)
        console.log('http://'+res.data.image_url[0].picture_url)
        console.log(res.data)
        var ret = []
        for (i=0 ; i<res.data.image_url.length ; i++) {
          ret.push({url: 'http://'+res.data.image_url[i].picture_url})
        }
        console.log(ret)
        this.imges = ret;
        console.log(';;;;;;;;;;;;;;;')
        // this.$emit('change', ret);
        if (res.data.success === true) {
          //todo:得到的结果放在srcList里进行展示
          this.status = 1;
          // this.srcList.push('http://' + res.data.image_url[0].picture_url)
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
          this.status = 0;
          // sleep(5000)    // 1s
        }
        console.log(i)
        console.log('a')
        sleep(500)    // 1s
        const data2 = new FormData()
        data2.append('user_id', this.$cookies.get("userId"))
        await this.request.post('http://101.42.173.97:8090/get_img_to_txt_process', data2).then(res => {
          console.log(res.data.process)
          console.log('b')
          this.status = 0;
          this.proValue = res.data.process//done:改成赋值给proValue(进度条数值)
          i = res.data.process
        })
      }
      console.log('txt2hand');
    }

  },
  components:{
    PictureCard
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
  height: 300px;
}
</style>
