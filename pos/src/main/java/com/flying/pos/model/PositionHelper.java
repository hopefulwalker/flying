package com.flying.pos.model;

import com.flying.common.model.ICyType;
import com.flying.util.math.IntegerUtils;

import java.util.List;

public final class PositionHelper {

    public static double getAvTransBal(List<Position> positions) {
        int curDate = IntegerUtils.getDate(System.currentTimeMillis());
        return getAvTransBal(positions, ICyType.CNY, curDate, curDate);
    }

    public static double getAvTransBal(List<Position> positions, short cyCode) {
        int curDate = IntegerUtils.getDate(System.currentTimeMillis());
        return getAvTransBal(positions, cyCode, curDate, curDate);
    }

    public static double getAvTransBal(List<Position> positions, short cyCode, int targetDate, int settleDate) {
        if (targetDate > settleDate)
            return 0;
        double avSize = 0;
        int bizDate = 0;
        int curDate = IntegerUtils.getDate(System.currentTimeMillis());
        for (Position pos : positions) {
            if (pos.getCyType() != cyCode || pos.getType() != IPosType.SETTLEBAL)
                continue;
            bizDate = pos.getBizDate();
            if (bizDate > targetDate)
                continue;
            if (pos.getSettleFlag() == ISettleFlag.SETTLED) {
                if ((bizDate == targetDate) || (targetDate > curDate && bizDate == curDate)) {
                    avSize = avSize + pos.getNos() - pos.getFrozenNos();
                }
            } else if (pos.getSettleFlag() == ISettleFlag.UNSETTLED) {
                if (pos.getSettleDate() > targetDate || pos.getSettleDate() > curDate) {
                    if (pos.getNos() < 0 || pos.getSettleDate() <= settleDate)
                        avSize = avSize + pos.getNos() - pos.getFrozenNos();
                }
            }
        }
        return avSize;
    }

    public static double getAvTradeBal(List<Position> positions) {
        int curDate = IntegerUtils.getDate(System.currentTimeMillis());
        return getAvTradeBal(positions, ICyType.CNY, curDate, curDate);
    }

    public static double getAvTradeBal(List<Position> positions, short cyCode) {
        int curDate = IntegerUtils.getDate(System.currentTimeMillis());
        return getAvTradeBal(positions, cyCode, curDate, curDate);
    }

    public static double getAvTradeBal(List<Position> positions, short cyCode, int targetDate, int settleDate) {
        if (targetDate > settleDate)
            return 0;
        double avSize = 0;
        int bizDate = 0;
        int curDate = IntegerUtils.getDate(System.currentTimeMillis());
        for (Position pos : positions) {
            if (pos.getCyType() != cyCode || pos.getType() != IPosType.AVBAL)
                continue;
            bizDate = pos.getBizDate();
            if (bizDate > targetDate)
                continue;
            if (pos.getSettleFlag() == ISettleFlag.SETTLED) {
                if ((bizDate == targetDate) || (targetDate > curDate && bizDate == curDate)) {
                    avSize = avSize + pos.getNos() - pos.getFrozenNos();
                }
            } else if (pos.getSettleFlag() == ISettleFlag.UNSETTLED) {
                if (pos.getSettleDate() > targetDate || pos.getSettleDate() > curDate) {
                    if (pos.getNos() < 0 || pos.getSettleDate() <= settleDate)
                        avSize = avSize + pos.getNos() - pos.getFrozenNos();
                }
            }
        }
        return avSize;
    }
}