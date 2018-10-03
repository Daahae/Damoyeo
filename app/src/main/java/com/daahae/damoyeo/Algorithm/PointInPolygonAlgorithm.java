package com.daahae.damoyeo.Algorithm;

import com.daahae.damoyeo.model.Position;

import java.util.ArrayList;

// 다각형 안의 점 탐지 알고리즘
public class PointInPolygonAlgorithm {
    private ArrayList<Line> polygonLines = null;

    public PointInPolygonAlgorithm(){}

    public PointInPolygonAlgorithm(ArrayList<Position> polygonPoints){
        initPolygonLines(polygonPoints);
    }

    private void initPolygonLines(ArrayList<Position> polygonPoints){

        if(polygonLines == null)
            polygonLines = new ArrayList<>();
        else
            polygonLines.clear();

        if(polygonPoints == null)
            return;

        for(int i=0; i<polygonPoints.size(); i++){
            int j = (i + 1) % polygonPoints.size();
            polygonLines.add(new Line(polygonPoints.get(i), polygonPoints.get(j)));
        }
    }

    public boolean isInside(Position target, ArrayList<Position> polygonPoints)
    {
        initPolygonLines(polygonPoints);

        int intersectTotalCnt = 0;
        for(Line line : polygonLines){
            if(isIntersect(target, line))
                intersectTotalCnt++;
        }
        return intersectTotalCnt % 2 == 1;
    }

    private boolean isIntersect(Position target, Line line){
        Position source = line.getSource();
        Position dest = line.getDest();

        //경도 y
        if((target.getLongitude() < source.getLongitude()) != (target.getLongitude() < dest.getLongitude())){
            double intersectionPoint = (dest.getLatitude() - source.getLatitude())
                    * (target.getLongitude() - source.getLongitude()) / (dest.getLongitude() - source.getLongitude())
                    + source.getLatitude();
            return target.getLatitude() < intersectionPoint;
        }
        return false;
    }

    class Line {
        private Position source, dest;

        Line(Position source, Position dest) {
            this.source = source;
            this.dest = dest;
        }

        Position getSource() {
            return source;
        }

        Position getDest() {
            return dest;
        }
    }
}