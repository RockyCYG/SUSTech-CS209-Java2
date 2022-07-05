<template>
  <div id="myChart3" style="margin-top: 10px;">

  </div>
</template>

<script setup lang="ts">
import * as echarts from 'echarts'
import { ECharts, EChartsOption } from 'echarts';
import { onMounted, onUpdated, ref } from "vue";
import request from '../../utils/request';

type TagViewSum = {
  name: string,
  tagTotalNum: Record<string, number>;
}

let tagViewSumList: TagViewSum[] = [];

onMounted(() => {
  initECharts();
})

onUpdated(() => {

})

let myChart: ECharts;

const initECharts = () => {
  myChart = echarts.init(document.querySelector("#myChart3")!);
  request.get("/stackoverflow/chart/tagnum", {
    params: {

    }
  }).then(res => {
    tagViewSumList = res.data;

    // console.log(tagViewSumList);
    // let arr = tagViewSumList.map(v => v.tagTotalNum)
    // console.log(arr);
    // let nums = arr.map((value, index, array) => {
    //   for (const year in value) {
    //     console.log(year);
    //     console.log(value[year]);
    //   }
    // })
    // console.log(nums);

    const series = [];

    tagViewSumList.forEach((value, index) => {
      const data: number[] = [];
      for (const year in value.tagTotalNum) {
        data.push(value.tagTotalNum[year]);
      }
      series.push({
        name: value.name,
        type: 'line',
        stack: 'Total',
        data: data
      })
    })

    console.log(series)

    const option: EChartsOption = {
      title: {
        text: 'The created-time distribution of the most popular 10 topics',
        textAlign: "left",
        left: "center",
        top: "0px",
        textStyle: {
          fontFamily: "'Open Sans Condensed', sans-serif",
          fontSize: 40,
          fontStyle: 'italic',
          fontWeight: 'bold'
        }
      },
      tooltip: {
        trigger: 'axis'
      },
      legend: {
        top: "10%"
      },
      grid: {
        left: '15%',
        right: '15%',
        bottom: '1%',
        top: '20%',
        containLabel: true
      },
      toolbox: {
        feature: {
          saveAsImage: {}
        }
      },
      xAxis: {
        type: 'category',
        boundaryGap: false,
        data: [2010, 2011, 2012, 2013, 2014, 2015, 2016, 2017, 2018, 2019, 2020, 2021, 2022]
      },
      yAxis: {
        type: 'value'
      },
      series: series
      // [
      // {
      //   name: 'Email',
      //   type: 'line',
      //   stack: 'Total',
      //   data: [120, 132, 101, 134, 90, 230, 210]
      // }
      // ]
    };

    option && myChart.setOption(option);
  })

}



</script>

<style>
#myChart3 {
  height: 600px;
}
</style>