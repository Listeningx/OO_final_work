<template>
<div id="root" >
  <div id="app" class="horizontal-gradient" v-if="this.$route.path=='/'" :style="bgImg">
  
    <div id="title" @click="enlarge('title')" >
      PENALTY <br>
      STOPPER
    </div>
    <!-- <div id="panel" data-animate="slideInRight" class="slow bounceIn">NIHAO</div> -->
    <button id="transitioner" @click="transit('transitioner')" style="font-family: 'Chill Pixels Matrix'; font-size: 40px;position:relative;top:100px;left: 300px;">START NOW</button>
    <img id="xiaogui" src="./assets/boo-icon-256.png"  alt="小鬼" style="position: relative; top:-50px;left:-380px;z-index:0">
    <img id="mario" src="./assets/luigui-icon-256.png" alt="绿色马里奥"  style="position:relative; top:-170px;left:270px;z-index:0;width: 380px;">
    <img id="star" src="./assets/start-icon-256.png" @click="spin('star')" alt="星星" style="position:absolute;top:100px;left:200px">
  
  </div>
   <router-view v-else/>
</div>
</template>

<script src="js/vendor/jquery.js"></script>
<script src="js/vendor/motion-ui.js"></script>
<script src="js/foundation.min.js"></script>
<script src="js/script.js"></script>

<script>
const url = require("./assets/backgrounds/blue.jpg");
  export default{
    data(){
      return{
        bgImg: {
        backgroundImage: "url(" + url + ")"
      }
      }
    },
    methods:{
      transit(id){
         var idd = "#".concat(id);
         MotionUI.animateIn(idd, 'fade-in');
         this.$router.push('login');
      },
      enlarge(id){
         var idd = "#".concat(id);
         MotionUI.animateIn(idd, 'scale-in-up');
      },
     
      spin(id){
        
        // alert(id);
        console.log(id);
         var idd = "#".concat(id);
        MotionUI.animateIn(idd,'spin-in-ccw');
      },
 
    },
    mounted(){
      // console.log(this.$route.path);
       MotionUI.animateIn('#title', 'fade-in');
       MotionUI.animateIn('#transitioner', 'fade-in');
       MotionUI.animateIn('#star','slide-in-up');
       MotionUI.animateIn('#xiaogui','fade-in');
       MotionUI.animateIn('#mario','fade-in');
    },
    // beforeCreate () {
    // document.querySelector('body').setAttribute('style', 'background:url("/assets/backgrounds/main.jpg")')
    // },
    // beforeDestroy () {
    // document.querySelector('body').setAttribute('style', 'background:#81f')
    // }
  }
 
</script>

<style lang=scss scoped>
@import './styles/classes';
@import './styles/settings';
@import './styles/motion-ui';
@import './styles/transitions/fade';
#app {
  font-family: Avenir, Helvetica, Arial, sans-serif;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
  text-align: center;
  color: #2c3e50;
}

nav {
  padding: 30px;

  a {
    font-weight: bold;
    color: #2c3e50;

    &.router-link-exact-active {
      color: #42b983;
    }
  }
}


#title {
  font-family: 'Chill Pixels Matrix';
  font-size: 160px;
  color: #347962;
  position: relative;
  top: 150px;
}

.fade-in {
   @include mui-fade(in, $duration: 4s);
}

.slide-in-up{
   @include mui-slide(in, $duration: 1s, $direction: 'up');
}

.spin-in-ccw{
  @include mui-spin(in,);
}

.scale-in-up{
 @include mui-zoom(in,  0.5, 1);
}

</style>
