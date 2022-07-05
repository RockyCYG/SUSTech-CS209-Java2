<template>
  <div id="main">

  </div>
</template>

<script setup lang="ts">
import * as echarts from 'echarts';
import { onMounted } from 'vue';
import request from '../../utils/request';

var app: any = {};
type EChartsOption = echarts.EChartsOption;

type ReposTopic = {
  topic: string,
  totalCount: number,
  lastYearCount: number
  lastMonthCount: number
}

let topicList: ReposTopic[] = [];

onMounted(() => {
  var chartDom = document.getElementById('main')!;
  var myChart = echarts.init(chartDom);
  var option: EChartsOption;

  request.get("/github/chart/topic", {
    params: {}
  }).then(res => {
    topicList = res.data;
    console.log(topicList);


    const posList = [
      'left',
      'right',
      'top',
      'bottom',
      'inside',
      'insideTop',
      'insideLeft',
      'insideRight',
      'insideBottom',
      'insideTopLeft',
      'insideTopRight',
      'insideBottomLeft',
      'insideBottomRight'
    ] as const;

    app.configParameters = {
      rotate: {
        min: -90,
        max: 90
      },
      align: {
        options: {
          left: 'left',
          center: 'center',
          right: 'right'
        }
      },
      verticalAlign: {
        options: {
          top: 'top',
          middle: 'middle',
          bottom: 'bottom'
        }
      },
      position: {
        options: posList.reduce(function (map, pos) {
          map[pos] = pos;
          return map;
        }, {} as Record<string, string>)
      },
      distance: {
        min: 0,
        max: 100
      }
    };

    app.config = {
      rotate: 90,
      align: 'left',
      verticalAlign: 'middle',
      position: 'insideBottom',
      distance: 15,
      onChange: function () {
        const labelOption: BarLabelOption = {
          rotate: app.config.rotate as BarLabelOption['rotate'],
          align: app.config.align as BarLabelOption['align'],
          verticalAlign: app.config
            .verticalAlign as BarLabelOption['verticalAlign'],
          position: app.config.position as BarLabelOption['position'],
          distance: app.config.distance as BarLabelOption['distance']
        };
        myChart.setOption<echarts.EChartsOption>({
          series: [
            {
              label: labelOption
            },
            {
              label: labelOption
            },
            {
              label: labelOption
            },
            {
              label: labelOption
            }
          ]
        });
      }
    };

    type BarLabelOption = NonNullable<echarts.BarSeriesOption['label']>;

    const labelOption: BarLabelOption = {
      show: true,
      position: app.config.position as BarLabelOption['position'],
      distance: app.config.distance as BarLabelOption['distance'],
      align: app.config.align as BarLabelOption['align'],
      verticalAlign: app.config.verticalAlign as BarLabelOption['verticalAlign'],
      rotate: app.config.rotate as BarLabelOption['rotate'],
      formatter: '{c}  {name|{a}}',
      fontSize: 16,
      rich: {
        name: {}
      }
    };

    option = {
      tooltip: {
        trigger: 'axis',
        axisPointer: {
          type: 'shadow'
        }
      },
      title: {
        text: "recent activity VS all time of the top topics",
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
        data: ['All Time', 'Last half year', 'Last 1 month'],
        top: '6%'
      },
      toolbox: {
        show: true,
        orient: 'vertical',
        left: 'right',
        top: 'center',
        feature: {
          mark: { show: true },
          dataView: { show: true, readOnly: false },
          magicType: { show: true, type: ['line', 'bar', 'stack'] },
          restore: { show: true },
          saveAsImage: { show: true }
        }
      },
      grid: {
        // left: "16%",
        // right: "16%",
        // bottom: "20%",
        top: "10%",
        height: 550,
        containLabel: true
      },
      xAxis: [
        {
          type: 'category',
          axisTick: { show: false },
          data: topicList.map(v => v.topic)
        }
      ],
      yAxis:
      {
        show: true,
        type: 'value',
        name: 'Repos updated',
        nameTextStyle: {
          fontSize: 30,
          padding: [0, 0, 30, 0]
        },
        nameRotate: 90,
        nameLocation: 'middle'
      }
      ,
      series: [
        {
          name: 'All Time',
          type: 'bar',
          barGap: 0,
          label: labelOption,
          emphasis: {
            focus: 'series'
          },
          data: topicList.map(v => v.totalCount)
        },
        {
          name: 'Last half year',
          type: 'bar',
          label: labelOption,
          emphasis: {
            focus: 'series'
          },
          data: topicList.map(v => v.lastYearCount)
        },
        {
          name: 'Last 1 month',
          type: 'bar',
          label: labelOption,
          emphasis: {
            focus: 'series'
          },
          data: topicList.map(v => v.lastMonthCount)
        }
      ]
    };

    option && myChart.setOption(option);
  })
})


</script>

<style>
#main {
  height: 800px;
  padding-left: 100px;
  margin-top: 0px;
}
</style>