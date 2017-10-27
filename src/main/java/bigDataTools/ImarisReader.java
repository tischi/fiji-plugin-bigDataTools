package bigDataTools;

import ncsa.hdf.hdf5lib.H5;
import net.imglib2.FinalRealInterval;
import net.imglib2.Interval;
import net.imglib2.ops.parse.token.Int;

import java.util.ArrayList;

import static bigDataTools.Hdf5Utils.*;
import static bigDataTools.ImarisUtils.*;

public class ImarisReader {

    int file_id;

    public ImarisReader( String directory, String filename )
    {
        file_id = openFile( directory, filename );
    }

    public void closeFile()
    {
        H5.H5Fclose( file_id );
    }

    public ArrayList< String > readChannels( )
    {
        ArrayList < String > channelColors = new ArrayList<>();

        for ( int c = 0; ; ++c )
        {

            String color = readStringAttribute( file_id,
                    DATA_SET_INFO
                            + "/" + CHANNEL + c,
                    COLOR );

            if ( color == null ) break;

            channelColors.add( color );

        }

        return ( channelColors ) ;
    }

    public ArrayList< String > readTimePoints( )
    {
        ArrayList < String > timePoints = new ArrayList<>();

        for ( int t = 0; ; ++t )
        {

            String timePoint = readStringAttribute( file_id,
                    DATA_SET_INFO
                    + "/" + TIME_INFO ,
                    TIME_POINT_ATTRIBUTE + (t+1) );

            if ( timePoint == null ) break;

            timePoints.add( timePoint );

        }

        return ( timePoints ) ;
    }

    public ArrayList< long[] > readDimensions( )
    {
        ArrayList < long[] > dimensions = new ArrayList<>();

        int nr = Integer.parseInt( readStringAttribute( file_id,
                DATA_SET_INFO + "/" + IMAGE,
                RESOLUTION_LEVELS_ATTRIBUTE ).trim() );

        for ( int r = 0; r < nr; ++r )
        {
            long[] dimension = new long[3];
            for ( int d = 0; d < 3; ++d )
            {
                // number of pixels at different resolutions
                dimension[d] = Integer.parseInt(
                    readStringAttribute( file_id,
                        DATA_SET_INFO + "/" + IMAGE,
                        XYZ[d] + d ) );
            }
            dimensions.add( dimension );
        }


        /*
        for ( int r = 0; ; ++r )
        {

            String dataSetName = DATA_SET
                    + "/" + RESOLUTION_LEVEL + r
                    + "/" + TIME_POINT + 0
                    + "/" + CHANNEL + 0
                    + "/" + DATA;

            long[] dimension = getDataDimensions( file_id, dataSetName );

            if ( dimension == null ) break;


            dimensions.add( dimension );
        }
        */

        return ( dimensions ) ;
    }

    public FinalRealInterval readInterval()
    {

        double[] min = new double[3];
        double[] max = new double[3];

        String s;

        for ( int d = 0; d < 3; ++d )
        {
            // physical interval
            min[d] = Double.parseDouble( readStringAttribute( file_id, DATA_SET_INFO + "/" + IMAGE,
                    "ExtMax" + d ).trim() );

            max[d] = Double.parseDouble( readStringAttribute( file_id, DATA_SET_INFO + "/" + IMAGE,
                    "ExtMin" + d ).trim() );
        }

        FinalRealInterval interval = new FinalRealInterval( min, max );

        return ( interval ) ;
    }

    /*
    public ArrayList< ArrayList < String[] > > readDataSets( int nr, int nc, int nt )
    {
        ArrayList< ArrayList < String[] > > dataSets = new ArrayList<>();

        for ( int r = 0; r < nr  ; ++r )
        {
            for ( int c = 0; c < nc; ++c )
            {
                ArrayList < String[] > timePoints = new ArrayList<>();

                for ( int t = 0; t < nt; ++t )
                {
                    String[] dataSet = new String[3];
                    timePoints.add( dataSet );
                }
            }
        }

            for ( int t )
            String dataSetName = DATA_SET
                    + RESOLUTION_LEVEL + r
                    + TIME_POINT + 0
                    + CHANNEL + 0
                    + DATA;

            long[] dimension = getDataDimensions( file_id, dataSetName );

            if ( dimension == null ) break;

            dimensions.add( dimension );
        }

        return ( dimensions ) ;
    }
    */



}


