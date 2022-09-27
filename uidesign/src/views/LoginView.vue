<template>
  <div id="loginroot" :style="bgImg ">
  <div class="whiteboard" >
    <login />
  </div>
    
  
   
  </div>
</template>


<script>
import login from "../components/LoginComponent"
const url = require("../assets/backgrounds/login.jpg");
  export default{
    data(){
      return{
        bgImg: {
        backgroundImage: "url(" + url + ")",
        // filter:"blur(2px)",
        height: "889px",
        width:"100%"
      }
      }
    },
    components:{
        login
    }
  }
 
</script>

<style>
.whiteboard{
  width: 500px; height: 500px;   
    position: absolute; left: 50%; top: 50%;   
    transform: translate(-50%, -50%);  /* 50%为自身尺寸的一半 */ 
  background-color: rgb(198, 230, 235);
  opacity: 0.8;
  /* filter: blur(5px); */
}
.whiteboard::before{
 
  filter: blur(5px);
}
</style>


