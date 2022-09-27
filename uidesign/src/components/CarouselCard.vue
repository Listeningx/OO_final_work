<template>
  <el-carousel indicator-position="outside" height="760px" >
    <el-carousel-item v-for="item in imges" :key="item">
      <img :src="item.url">
    </el-carousel-item>
  </el-carousel>
</template>
<script>

    export default {
        // name: "zouma",
      data () {
        return {
        }
      },
      props: {
        imges: {
          type: Array, //数据类型有 String、Number、Boolean、Array ...
          default: () => []
        }
      }
    }
</script>
<style>
.el-carousel{
  width:730px
}

/* .el-carousel__item:nth-child(2n) {
    background-color: #99a9bf;
    background-image: url("../assets/backgrounds/main.jpg");
  } */
  /* .el-carousel__item h3 {
    color: #475669;
    font-size: 18px;
    opacity: 0.75;
    line-height: 900px;
    margin: 0;
  }

  .el-carousel__item:nth-child(2n) {
    background-color: #99a9bf;
    background-image: url("../assets/backgrounds/main.jpg");
  }

  .el-carousel__item:nth-child(2n+1) {
    background-color: #d3dce6;
  } */
</style>
