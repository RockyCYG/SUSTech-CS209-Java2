<template>
  <div id="pie">

  </div>
</template>

<script setup lang="ts">
import * as echarts from 'echarts';
import { onMounted } from 'vue';
import request from '../../utils/request';

type EChartsOption = echarts.EChartsOption;

type yearCount = {
  name: string,
  value: number
}

let yearCountList: yearCount[] = [];

onMounted(() => {
  var chartDom = document.getElementById('pie')!;
  var myChart = echarts.init(chartDom);
  var option: EChartsOption;

  request.get("/github/chart/piechart", {
    params: {}
  }).then(res => {

    yearCountList = res.data;

    option = {
      title: {
        text: "Github Repos created Each Year",
        textAlign: "left",
        left: "center",
        top: "100px",
        textStyle: {
          fontFamily: "'Open Sans Condensed', sans-serif",
          fontSize: 40,
          fontStyle: 'italic',
          fontWeight: 'bold'
        }
      },
      tooltip: {
        trigger: 'item',
        formatter: '{a} <br/>{b} : {c} ({d}%)'
      },
      legend: {
        left: 'center',
        top: '200',
        data: yearCountList.map(v => v.name).sort((a, b) => a - b)
      },
      toolbox: {
        show: true,
        feature: {
          mark: { show: true },
          dataView: { show: true, readOnly: false },
          restore: { show: true },
          saveAsImage: { show: true }
        }
      },
      series: [
        {
          name: 'Area Mode',
          type: 'pie',
          radius: [100, 200],
          center: ['50%', '50%'],
          roseType: 'area',
          itemStyle: {
            borderRadius: 10
          },
          data: yearCountList
        }
      ]
    };

    option && myChart.setOption(option);
  })

})


</script>

<style>
#pie {
  height: 1000px;
  margin-top: 60px;
}
</style>