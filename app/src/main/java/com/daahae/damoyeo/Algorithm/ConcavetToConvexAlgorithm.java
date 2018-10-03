package com.daahae.damoyeo.Algorithm;

import com.daahae.damoyeo.model.Position;

import java.util.ArrayList;

public class ConcavetToConvexAlgorithm {

    private ArrayList<Position> polygonPositions;

    public ConcavetToConvexAlgorithm(){
    }

    public ArrayList<Position> convert(ArrayList<Position> polygonPositions){
        PointInPolygonAlgorithm pointInPolygonAlgorithm = new PointInPolygonAlgorithm();

        for(int i=polygonPositions.size() - 1; i >= 0; i--){
            Position target = polygonPositions.get(i);
            polygonPositions.remove(i);

            if(pointInPolygonAlgorithm.isInside(target, polygonPositions))
                polygonPositions.remove(i);
        }
        return polygonPositions;
    }
}
