<template>
  <div id="myChart2" style="height: 800px; margin-top: 100px">

  </div>
</template>

<script setup lang="ts">
import * as echarts from "echarts";
import { ECharts, EChartsOption } from "echarts";
import { onMounted, onUpdated, ref } from "vue";
import request from "../../utils/request";

type Tag = {
  name: string,
  scoreCount: number,
  viewCount: number
}

let myChart: ECharts;

let reposNum: number[] = [];

const colorList: string[] = [
  '#c23531',
  '#2f4554',
  '#61a0a8',
  '#d48265',
  '#91c7ae',
  '#749f83',
  '#ca8622',
  '#bda29a',
  '#6e7074',
  '#546570'
];

onMounted(() => {
  initECharts();
})

onUpdated(() => {
  initECharts();
})

const initECharts = () => {
  myChart = echarts.init(document.getElementById("myChart2")!);

  request.get("/github/chart/forkrepos", {
    params: {}
  }).then(res => {
    reposNum = res.data;

    const option: EChartsOption = {
      tooltip: {
        trigger: "axis",
        axisPointer: {
          type: "shadow"
        }
      },
      title: {
        text: "Github Repos Number——Forks",
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
      legend: {
        orient: "horizontal",
        left: "center",
        top: "6%"
      },
      grid: {
        left: "16%",
        right: "16%",
        bottom: "20%",
        top: "15%",
        height: 450,
        containLabel: true
      },
      xAxis: [
        {
          type: "category",
          data: ['0-100', '100-200', '200-300', '300-400', '400-500', '500-600', '600-700', '700-800', '800-900', '900-1000', '1000-40000'],
          name: 'Forks in Range',
          nameTextStyle: {
            fontSize: 30,
            padding: [30, 0, 0, 0]
          },
          nameRotate: 0,
          nameLocation: 'middle'
        }
      ],
      yAxis: [
        {
          show: true,
          type: 'value',
          name: 'Number of Repos',
          nameTextStyle: {
            fontSize: 30,
            padding: [0, 0, 50, 0]
          },
          nameRotate: 90,
          nameLocation: 'middle'
        }
      ],
      series: [
        {
          name: "View on Github",
          type: "bar",
          emphasis: {
            focus: "series"
          },
          itemStyle: {
            color: (param) => {
              return colorList[param.dataIndex % 10]
            }
          },
          data: reposNum
        },
        // {
        //   name: "viewCount",
        //   type: "bar",
        //   emphasis: {
        //     focus: "series"
        //   },
        //   data: viewCountList
        // }
      ]
    };

    option && myChart.setOption(option);
  })

}

</script>

<style>
</style>