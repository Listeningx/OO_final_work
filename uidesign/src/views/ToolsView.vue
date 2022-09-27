<template>
  <div id="toolsroot" :style="bgImg ">
  <span id="pictureCard" class="block">
    <div class="block" id="avator" @click="jump2center"><el-avatar :size="80" :src="circleUrl" ></el-avatar></div>
    <el-select v-model="value" placeholder="请选择功能" style="position: absolute;top:30px;left:100px">
      <el-option
          v-for="item in funcs"
          :key="item.value"
          :label="item.label"
          :value="item.value">
      </el-option>
    </el-select>
    <div v-if="!status" style="position:absolute;top:300px;left:345px;font-weight: 700;">加载进度</div>
  </span>

    <div id="textUpload" v-if="value">
      <TextComponent ref="tc" @change="costPlannedAmountChange"></TextComponent>
    </div>

    <div id="picUpload" v-if="!value">
      <div class="upload" id="upload">
        <input type="file" id="file" @change="pic2hand($event)" />
        <TextComponent ref="tc" @change="costPlannedAmountChange"></TextComponent>
      </div>
    </div>
    <!-- <img src="../assets/icons/pic2hand.png" alt="蓝鸟" @click="pic2hand" style="position: absolute;width: 200px;left:180px;top:100px"> -->
    <!-- <img src="../assets/icons/txt2hand.png" alt="绿鸟" @click="txt2hand" style="position: absolute;width: 300px;left:1040px;top:380px"> -->

  </div>

</template>

<script>
import TextComponent from "../components/TextComponent.vue";
// import circleUrl from "../views/HistoryView.vue";
// import PictureCard from "@/components/CarouselCard.vue";
// import PictureComponent from "@/components/PictureComponent.vue";
import Vue from 'vue'
var Event = new Vue();

function sleep(numberMillis) {
  var now = new Date();
  var exitTime = now.getTime() + numberMillis;
  while (now.getTime() < exitTime) {
    now = new Date();
  }
}


export default{
  data() {
    return {
      status:0,//status表示显示结果还是显示进度条，0显示进度条，1显示结果
      circleUrl:'',
      funcs:[
        {
          value: 0,
          label: '图片转手写'
        }, {
          value:  1,
          label: '文本转手写'
        }
      ],
      value:1,//value指示当前所处功能状态，1表示文本转手写，0表示图片转手写
      //options包含关于进度条的样式
      // options: {
      //     text: {
      //       color: '#FFFFFF',
      //       shadowEnable: true,
      //       shadowColor: '#000000',
      //       fontSize: 14,
      //       fontFamily: 'Helvetica',
      //       dynamicPosition: false,
      //       hideText: false
      //     },
      //     progress: {
      //       color: '#71d0a8',
      //       backgroundColor: '#457341',
      //       inverted: false
      //     },
      //     layout: {
      //       height: 100,
      //       width: 140,
      //       verticalTextAlign: 61,
      //       horizontalTextAlign: 43,
      //       zeroOffset: 0,
      //       strokeWidth: 300,
      //       progressPadding: 0,
      //       type: 'cylinder',
      //       position:'absolute',
      //       left:'300px',
      //     }
      //   },
      //  proValue:'0',//进度条数值
      form: {
        title: '',
        user_id: '',
        color: '',
        text: '',
        jwt:'',
        progress:''
      },
      pic:{
        picture:'',
        txt:'',
        jwt:'',
      },
      image_url:[

        {
          "picture_url": "localhost:8090/images/8/158/img0.png",
          "picture_num": 0
        },
        {
          "picture_url": "localhost:8090/images/8/158/img1.png",
          "picture_num": 1
        }

      ],
      // url: 'https://fuss10.elemecdn.com/e/5d/4a731a90594a4af544c0c25941171jpeg.jpeg',
      // srcList: [
      // 'https://fuss10.elemecdn.com/8/27/f01c15bb73e1ef3793e64e6b7bbccjpeg.jpeg',
      // 'https://fuss10.elemecdn.com/1/8e/aeffeb4de74e2fde4bd74fc7b4486jpeg.jpeg'
      // ],
      imges:[
        {url: require("../assets/backgrounds/blue.jpg")},
        {url: require("../assets/backgrounds/login.jpg")},
        {url: require("../assets/backgrounds/main.jpg")},
      ],//储存照片墙展示的图片url（就是文本转手写返回来的图片）

      bgImg: {
        // backgroundImage: "url(" + url + ")",
        // filter:"blur(2px)",
        height: "889px",
        width: "100%",
        opacity: "0.7",
        'background-repeat': "repeat-y",
      },
    };
  },
  mounted(){
    this.checkJWT();
    console.log(this.value);

    Event.$on('touxiang',circleUrl => {
         this.circleUrl = circleUrl;
         console.log('toools'+this.circleUrl);
     })
  },
  methods: {
    checkJWT() {
      if (this.$cookies.get("oatoken") === null) {
        console.log("checkJWT")
        console.log(this.$cookies.get("oatoken"))
        this.$message({
          message: '请先登录',
          type: 'warning'
        });
        this.$router.push('/login')
      }
    },
    async costPlannedAmountChange(param1) {
      console.log(param1)
      // console.log(123123123)
      // this.imges = param1
      // this.$refs.pictureCard.imges = param1
    },
    changeStatus(){
      this.status = !this.status;
    },
    pic2hand(res) {
      console.log('pic2hand');
      //todo:将上传文件发送请求，先得到文本form，再调用txt2hand得到图片（上传文件到在UploadComponent.vue文件中action后面的地址，待定，返回文件存储在pic里）
      this.form = this.$refs.tc.form;

      const data = new FormData()
      data.append('file', res.target.files[0])
      data.append('user_id', this.$cookies.get("userId"))
      data.append('jwt', this.$cookies.get("oatoken"))
      this.request.post('http://101.42.173.97:8090/max_img_to_txt', data).then(async res => {
        console.log('res', res)
        if (res.data.success === true) {
          console.log(res.data.txt)
          this.pic.txt = res.data.txt
          this.form.text = res.data.txt
        } else {
          this.$message({
            message: res.data.message,
            type: 'warning'
          });
        }
      })
      console.log('pic2hand');
    },
    async txt2hand() {
      console.log('txt2hand');
      console.log(this.data.form);
      //todo:将左侧信息发送请求，得到返回手写图片放在image_url里(左侧信息储存在以下from表单中，具体格式可见TextComponent.vue文件)
      this.form = this.$refs.tc.form;

      console.log(this.form.title);
      console.log(this.form.color);
      console.log(this.form.text);
      console.log(this.$cookies.get("userId"));
      console.log(this.$cookies.get("oatoken"));

      const data = new FormData()
      data.append('title', this.form.title)
      data.append('text', this.form.text)
      data.append('color', this.form.color)
      data.append('user_id', this.$cookies.get("userId"))
      data.append('jwt', this.$cookies.get("oatoken"))
      this.request.post('http://101.42.173.97:8090/text', data).then(async res => {
        console.log('res', res)
        if (res.data.success === true) {
          //todo:得到的结果放在srcList里进行展示
          this.srcList.push('http://' + res.data.image_url[0].picture_url)
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
        data2.append('user_id', this.$cookies.get("userId"))
        await this.request.post('http://101.42.173.97:8090/get_img_to_txt_process', data2).then(res => {
          console.log(res.data.process)
          console.log('b')
          this.proValue = res.data.process//done:改成赋值给proValue(进度条数值)
          i = res.data.process
        })
      }

      console.log('txt2hand');
    },

    jump2center(){
      //todo:跳转到/center
      console.log('jump2center')
      this.$router.push('/history')
    }

  },
  created(){
   Event.$on('touxiang',circleUrl => {
         this.circleUrl = circleUrl;
         console.log('toools'+this.circleUrl);
     })

  },
  components: { TextComponent}
}

</script>

<style>
#textUpload{
  /* max-height: 889px; */
  /* width: 50%; */
  /* background-image:url("../assets/backgrounds/main.jpg"); */
  position:absolute;
  top:30px;
  left:740px;
  height:720px;
  /* margin: auto; */
}
#upload{
  /* max-height: 889px; */
  /* width: 50%; */
  /* background-image:url("../assets/backgrounds/main.jpg"); */
  position:absolute;
  top:10px;
  left:740px;
  height:670px;
  /* margin: auto; */
}
#pictureCard{
  /* width: 100px; */
  height: fit-content;
  /* background-color: aquamarine; */
}
#toolsroot{
  background-image: url("../assets/backgrounds/main.jpg");
}
#cylinder-progress{
  /* width:300px; */
  position: relative;
  top: 330px;
  left:300px;
}
#avator{
  position: absolute;
  top:10px;
  left:10px;
}
</style>
