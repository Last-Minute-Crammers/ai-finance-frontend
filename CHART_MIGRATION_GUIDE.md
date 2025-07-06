# 图表库迁移指南

## 问题描述

在uni-app x项目中，`uni_modules/qiun-data-charts`库包含多个.vue文件，这些文件在uvue环境中会导致打包失败。

## 解决方案

我们提供了两种解决方案来解决这个问题：

### 方案1：将vue文件转换为uvue文件

我们已经创建了转换后的uvue版本组件：

#### 文件结构
```
components/
├── QiunDataCharts.uvue      # 主图表组件
├── QiunLoading.uvue         # 加载组件
├── QiunError.uvue           # 错误组件
└── ChartExample.uvue        # 使用示例
```

#### 使用方法

```vue
<template>
  <view>
    <QiunDataCharts
      :type="'line'"
      :chartData="chartData"
      :opts="chartOpts"
      :background="'#FFFFFF'"
      :loadingType="2"
      :errorShow="true"
    />
  </view>
</template>

<script>
import QiunDataCharts from '@/components/QiunDataCharts.uvue';

export default {
  components: {
    QiunDataCharts
  },
  data() {
    return {
      chartData: {
        categories: ['1月', '2月', '3月', '4月', '5月', '6月'],
        series: [
          {
            name: '收入',
            data: [120, 132, 101, 134, 90, 230]
          }
        ]
      },
      chartOpts: {
        color: ['#007AFF'],
        padding: [15, 15, 15, 15],
        legend: { show: true },
        xAxis: { disableGrid: false },
        yAxis: { gridType: 'dash', dashLength: 2 }
      }
    };
  }
};
</script>
```

### 方案2：使用轻量级原生图表组件

我们创建了一个全新的轻量级图表组件 `SimpleChart.uvue`，完全基于原生Canvas实现：

#### 特性
- ✅ 支持折线图、柱状图、饼图、面积图
- ✅ 完全兼容uvue环境
- ✅ 轻量级，无外部依赖
- ✅ 支持自定义颜色、图例、网格
- ✅ 支持触摸交互
- ✅ 内置加载和错误状态

#### 使用方法

```vue
<template>
  <view>
    <SimpleChart
      :type="'line'"
      :series="chartSeries"
      :categories="chartCategories"
      :width="350"
      :height="250"
      :colors="['#007AFF', '#34C759']"
      :showLegend="true"
      :showGrid="true"
    />
  </view>
</template>

<script>
import SimpleChart from '@/components/SimpleChart.uvue';

export default {
  components: {
    SimpleChart
  },
  data() {
    return {
      chartCategories: ['1月', '2月', '3月', '4月', '5月', '6月'],
      chartSeries: [
        {
          name: '收入',
          data: [120, 132, 101, 134, 90, 230]
        },
        {
          name: '支出',
          data: [220, 182, 191, 234, 290, 330]
        }
      ]
    };
  }
};
</script>
```

## 迁移步骤

### 步骤1：删除原有的uni_modules

```bash
# 删除原有的qiun-data-charts库
rm -rf uni_modules/qiun-data-charts
```

### 步骤2：选择方案并复制组件

#### 如果选择方案1（转换版本）：
```bash
# 复制转换后的组件到你的项目中
cp components/QiunDataCharts.uvue your-project/components/
cp components/QiunLoading.uvue your-project/components/
cp components/QiunError.uvue your-project/components/
```

#### 如果选择方案2（轻量级版本）：
```bash
# 复制新的图表组件到你的项目中
cp components/SimpleChart.uvue your-project/components/
```

### 步骤3：更新现有代码

#### 从qiun-data-charts迁移到QiunDataCharts.uvue

**原代码：**
```vue
<template>
  <qiun-data-charts
    :type="'line'"
    :chartData="chartData"
    :opts="chartOpts"
  />
</template>

<script>
export default {
  data() {
    return {
      chartData: {
        categories: ['1月', '2月', '3月'],
        series: [{ name: '数据', data: [100, 200, 300] }]
      }
    };
  }
};
</script>
```

**新代码：**
```vue
<template>
  <QiunDataCharts
    :type="'line'"
    :chartData="chartData"
    :opts="chartOpts"
  />
</template>

<script>
import QiunDataCharts from '@/components/QiunDataCharts.uvue';

export default {
  components: {
    QiunDataCharts
  },
  data() {
    return {
      chartData: {
        categories: ['1月', '2月', '3月'],
        series: [{ name: '数据', data: [100, 200, 300] }]
      }
    };
  }
};
</script>
```

#### 从qiun-data-charts迁移到SimpleChart.uvue

**原代码：**
```vue
<template>
  <qiun-data-charts
    :type="'line'"
    :chartData="chartData"
  />
</template>
```

**新代码：**
```vue
<template>
  <SimpleChart
    :type="'line'"
    :series="chartSeries"
    :categories="chartCategories"
    :width="350"
    :height="250"
  />
</template>

<script>
import SimpleChart from '@/components/SimpleChart.uvue';

export default {
  components: {
    SimpleChart
  },
  data() {
    return {
      chartCategories: ['1月', '2月', '3月'],
      chartSeries: [{ name: '数据', data: [100, 200, 300] }]
    };
  }
};
</script>
```

## 数据格式转换

### qiun-data-charts格式 → SimpleChart格式

**qiun-data-charts格式：**
```javascript
{
  categories: ['1月', '2月', '3月'],
  series: [
    { name: '系列1', data: [100, 200, 300] },
    { name: '系列2', data: [150, 250, 350] }
  ]
}
```

**SimpleChart格式：**
```javascript
// categories和series分开传递
categories: ['1月', '2月', '3月'],
series: [
  { name: '系列1', data: [100, 200, 300] },
  { name: '系列2', data: [150, 250, 350] }
]
```

## 测试验证

使用提供的 `ChartExample.uvue` 组件来测试两种方案：

```vue
<template>
  <ChartExample />
</template>

<script>
import ChartExample from '@/components/ChartExample.uvue';

export default {
  components: {
    ChartExample
  }
};
</script>
```

## 注意事项

1. **性能考虑**：SimpleChart组件更轻量，适合简单图表；QiunDataCharts功能更丰富，适合复杂图表
2. **兼容性**：两种方案都完全兼容uvue环境
3. **维护性**：SimpleChart更容易维护和定制
4. **功能对比**：
   - SimpleChart：基础图表类型，轻量级
   - QiunDataCharts：完整功能，支持更多配置选项

## 推荐方案

- **新项目**：推荐使用 `SimpleChart.uvue`
- **现有项目**：可以选择 `QiunDataCharts.uvue` 以保持API兼容性
- **性能要求高**：推荐使用 `SimpleChart.uvue`

## 故障排除

如果遇到问题：

1. 确保组件路径正确
2. 检查数据格式是否符合要求
3. 查看控制台错误信息
4. 参考 `ChartExample.uvue` 的使用方式 