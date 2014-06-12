(ns server.workbook
  (:require [dk.ative.docjure.spreadsheet :as xls]
            [iroh.core :refer :all])
  (:import [org.apache.poi.ss.usermodel Row Sheet Cell]
           org.apache.poi.xssf.usermodel.XSSFWorkbook
           org.apache.poi.ss.usermodel.charts.DataSources
           org.apache.poi.hssf.util.CellRangeAddress))

(def wb (XSSFWorkbook.))
;;(.? wb :name)

(def sheet (.createSheet wb "Sheet 2"))
;;(>ns xls.worksheet org.apache.poi.xssf.usermodel.XSSFSheet)

(def ROWS 3)
(def COLS 10)

(doseq [i (range ROWS)]
  (let [row (.createRow sheet i)]
    (doseq [j (range COLS)]
      (let [cell (.createCell row j)]
        (.setCellType cell org.apache.poi.ss.usermodel.Cell/CELL_TYPE_NUMERIC)
        (.setCellValue cell (double (* j (inc i))))))))

(def drawing (.createDrawingPatriarch sheet))
(def anchor (.createAnchor drawing 0 0 0 0 0 5 10 15))

(def chart (.createChart drawing anchor))
(def legend (.getOrCreateLegend chart))
(.setPosition legend org.apache.poi.ss.usermodel.charts.LegendPosition/TOP_RIGHT)

(def data (-> chart (.getChartDataFactory) (.createScatterChartData)))
(def bottomAxis
  (-> chart (.getChartAxisFactory) (.createValueAxis org.apache.poi.ss.usermodel.charts.AxisPosition/BOTTOM)))
(def leftAxis
  (-> chart (.getChartAxisFactory) (.createValueAxis org.apache.poi.ss.usermodel.charts.AxisPosition/LEFT)))
(.setCrosses leftAxis org.apache.poi.ss.usermodel.charts.AxisCrosses/AUTO_ZERO)

(def xs (DataSources/fromNumericCellRange sheet (CellRangeAddress. 0 0 0 (dec COLS))))
(doseq [i (range 1 ROWS)]
  (.addSerie data xs (DataSources/fromNumericCellRange sheet (CellRangeAddress. i i 0 (dec COLS)))))

(let [axis-arr (make-array (type leftAxis) 2)]
  (aset axis-arr 0 bottomAxis)
  (aset axis-arr 1 leftAxis)
  (.plot chart data  axis-arr))

(xls/save-workbook! "graph.xlsx" wb)
