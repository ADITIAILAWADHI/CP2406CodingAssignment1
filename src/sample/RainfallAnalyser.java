package sample;

public class RainfallAnalyser {
    public static void main(String[] args) {

//        var path = "resources/MountSheridanStationCNS.csv";
        System.out.print("Enter path name: ");
        var path = textio.TextIO.getln();

        try {
            int count = csvWordCount(path);
            textio.TextIO.readFile(path);
            analyseDataset(path, count);
        } catch (Exception e) {
            System.out.println("ERROR: failed to process file");
        }
    }

    private static int csvWordCount(String path) {
        textio.TextIO.readFile(path);
        var row = extractRecord();
        int count=0;
        while (row != null) {
            count++;
            row = extractRecord();
        }
        return count;
    }

    private static void analyseDataset(String path, int count) {
        var header = extractRecord(); // ignore header record
        if (header == null) {
            System.out.println("ERROR: file is empty");
            return;
        }

        // setup accumulation
        var totalRainfall = 0.0;
        var minRainfall = Double.MAX_VALUE;
        var maxRainfall = Double.MIN_VALUE;
        var currentMonth = 1; // sentinel
        var year = 0;

        // setup output file
        String savePath = generateSavePath(path);
        textio.TextIO.writeFile(savePath);

        // write header record
        textio.TextIO.putln("year,month,total,min,max");

        var record = extractRecord(); // get record
        // create new records for save file
        for (int i = 1; i < count; i++) {
            if (record != null) {
                // convert important fields to correct data types
                year = Integer.parseInt(record[2]);
                var month = Integer.parseInt(record[3]);
                var day = Integer.parseInt(record[4]);
                var rainfall = record.length < 6 ? 0 : Double.parseDouble(record[5]);

                if ((month < 1 || month > 12) || (day < 1 || day > 31)) {
                    System.out.println("ERROR: failed to process file");
                    return;
                }

                if (month != currentMonth) {
                    // new month detected - save record and reset accumulation
                    writeRecord(totalRainfall, minRainfall, maxRainfall, currentMonth, year);

                    currentMonth = month;
                    totalRainfall = 0;
                    minRainfall = Double.MAX_VALUE;
                    maxRainfall = Double.MIN_VALUE;
                }

                // update accumulation
                totalRainfall += rainfall;
                if (rainfall < minRainfall) minRainfall = rainfall;
                if (rainfall > maxRainfall) maxRainfall = rainfall;
            }
            record = extractRecord(); // get record
        }

        if (currentMonth < 12) {
            // last month is incomplete - save record
            writeRecord(totalRainfall, minRainfall, maxRainfall, currentMonth, year);
        }
    }

    private static String generateSavePath(String path) {
        var pathElements = path.trim().split("/");
        var filenameElements = pathElements[1].split("\\.");
        return String.format("%s/%s_analysed.%s", pathElements[0],
                filenameElements[0], filenameElements[1]);
    }

    private static void writeRecord(double totalRainfall, double minRainfall, double maxRainfall, int currentMonth, int year) {
        var newRecord = String.format("%d,%d,%1.2f,%1.2f,%1.2f%s", year, currentMonth,
                totalRainfall, minRainfall, maxRainfall, System.lineSeparator());
        textio.TextIO.putf(newRecord);
    }

    private static String[] extractRecord() {
        if (textio.TextIO.eof()) return null; // convert EOF to null

        var text = textio.TextIO.getln();
        return text.trim().split(",");
    }
}
