# csv2chart

A simple command line tool to convert csv files into charts.

Chart rendering is done in JFreeChart.

It parses the csv file and decides on which chart to render based on the data, comments in the csv file, properties files and command line options.

## Installation

After downloading the distribution file from the releases, extract the content in an appropriate folder and set the PATH variable to the 'bin' directory of the extracted folder. 

## Usage

The command line tool is called with the list of csv files to be converted.

Each csv file may contain leading comments to control the generated chart.

Example:
```csv
# csv2chart.title=Sales - Sections
# csv2chart.chart=pie
#
Section, Revenue
 Mobile,   12000
    Web,    7000
 Stores,    9000
```

It is also possible to put the options into a properties files with the same basename as the csv file.

File: `SectionAnalysis.csv`
```csv
Section, Expenses, Revenue
 Mobile,    4000,     6000
    Web,    2000,     7000         
 Stores,   12000,    14000
```

File: `SectionAnalysis.properties`
```properties
chart=bar
```


## Options

    --properties filename
        Loads the specified properties file.

    --out-prefix fileprefix
        Prefix for output chart files

    --out-postfix filepostfix
        Postfix for output chart files

    --chart charttype
        The chart type to generate.
        Supported types: auto, line, bar, xyline, pie, bubble
        Default: 'auto'

    --title text
        Text to appear as title in the chart.

    --no-header-column
        When specified the first column is not interpreted as headers

    --no-row-column
        When specified the first row is not interpreted as headers

    --x-axis text
        Text to appear as x-axis label.

    --y-axis text
        Text to appear as y-axis label.

    --width pixels
        The width of the generated charts in pixels.
        Default: 800

    --height pixels
        The height of the generated charts in pixels.
        Default: 600


## Example Charts

### Line Chart

[Revenue.csv](https://raw.githubusercontent.com/eobermuhlner/csv2chart/master/ch.obermuhlner.csv2chart.example/data/Revenue.csv)
![Line Chart](https://raw.githubusercontent.com/eobermuhlner/csv2chart/master/ch.obermuhlner.csv2chart.example/data/Revenue.png)

[sin.csv](https://raw.githubusercontent.com/eobermuhlner/csv2chart/master/ch.obermuhlner.csv2chart.example/data/sin.csv)
![XY Line Chart](https://raw.githubusercontent.com/eobermuhlner/csv2chart/master/ch.obermuhlner.csv2chart.example/data/sin.png)

### Bar Chart

[SectionAnalysis.csv](https://raw.githubusercontent.com/eobermuhlner/csv2chart/master/ch.obermuhlner.csv2chart.example/data/SectionAnalysis.csv)
![Bar Chart](https://raw.githubusercontent.com/eobermuhlner/csv2chart/master/ch.obermuhlner.csv2chart.example/data/SectionAnalysis.png)

### Pie Chart

[SalesPie.csv](https://raw.githubusercontent.com/eobermuhlner/csv2chart/master/ch.obermuhlner.csv2chart.example/data/SalesPie.csv)
![Pie Chart](https://raw.githubusercontent.com/eobermuhlner/csv2chart/master/ch.obermuhlner.csv2chart.example/data/SalesPie.png)

### Bubble Chart

[Crime2005.csv](https://raw.githubusercontent.com/eobermuhlner/csv2chart/master/ch.obermuhlner.csv2chart.example/data/Crime2005.csv)
![Bubble Chart](https://raw.githubusercontent.com/eobermuhlner/csv2chart/master/ch.obermuhlner.csv2chart.example/data/Crime2005.png)
