package com.morak.back.core.exception;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public enum CustomErrorCode {

    EXPIRED_AUTHORIZATION_ERROR("0200", "사용자 인증에 실패했습니다."),
    INVALID_AUTHORIZATION_ERROR("0201", "사용자 인증에 실패했습니다."),
    GITHUB_AUTHORIZATION_ERROR("0202", "사용자 인증에 실패했습니다."),
    EMPTY_AUTHORIZATION_ERROR("0203", "사용자 인증에 실패했습니다."),

    MEMBER_NOT_FOUND_ERROR("0300", "요청한 리소스를 찾을 수 없습니다."),

    TEAM_INVITATION_EXPIRED_ERROR("1100", "만료된 초대코드입니다."),
    TEAM_ALREADY_JOINED_ERROR("1101", "팀에 이미 속해있습니다."),
    TEAM_MEMBER_MISMATCHED_ERROR("1200", "접근 권한이 없는 요청입니다."),
    TEAM_NOT_FOUND_ERROR("1300", "요청한 리소스를 찾을 수 없습니다."),
    TEAM_INVITATION_NOT_FOUND_ERROR("1301", "요청한 리소스를 찾을 수 없습니다."),

    POLL_ALREADY_CLOSED_ERROR("2100", "잘못된 요청입니다."),
    POLL_COUNT_OVER_ERROR("2101", "잘못된 요청입니다."),
    POLL_TEAM_MISMATCHED_ERROR("2200", "접근 권한이 없는 요청입니다."),
    POLL_MEMBER_MISMATCHED_ERROR("2201", "접근 권한이 없는 요청입니다."),
    POLL_ITEM_MISMATCHED_ERROR("2202", "접근 권한이 없는 요청입니다."),
    POLL_NOT_FOUND_ERROR("2300", "요청한 리소스를 찾을 수 없습니다."),
    POLL_ITEM_NOT_FOUND_ERROR("2301", "요청한 리소스를 찾을 수 없습니다."),

    APPOINTMENT_ALREADY_CLOSED_ERROR("3100", "잘못된 요청입니다."),
    APPOINTMENT_DUPLICATED_AVAILABLE_TIME_ERROR("3101", "잘못된 요청입니다."),
    APPOINTMENT_PAST_CREATE_ERROR("3102", "잘못된 요청입니다."),
    APPOINTMENT_DURATION_OVER_TIME_PERIOD_ERROR("3103", "잘못된 요청입니다."),
    APPOINTMENT_DURATION_NOT_MINUTES_UNIT_ERROR("3104", "잘못된 요청입니다."),
    APPOINTMENT_DURATION_HOUR_OVER_DAY_ERROR("3105", "잘못된 요청입니다."),
    APPOINTMENT_DURATION_MINUTE_OVER_HOUR_ERROR("3106", "잘못된 요청입니다."),
    APPOINTMENT_DURATION_MINUTE_RANGE_ERROR("3107", "잘못된 요청입니다."),
    APPOINTMENT_PAST_DATE_CREATE_ERROR("3108", "잘못된 요청입니다."),
    APPOINTMENT_DATE_REVERSE_CHRONOLOGY_ERROR("3109", "잘못된 요청입니다."),
    APPOINTMENT_TIME_REVERSE_CHRONOLOGY_ERROR("3110", "잘못된 요청입니다."),
    APPOINTMENT_NOT_DIVIDED_BY_MINUTES_UNIT_ERROR("3111", "잘못된 요청입니다."),
    AVAILABLETIME_DATE_OUT_OF_RANGE_ERROR("3112", "잘못된 요청입니다."),
    AVAILABLETIME_TIME_OUT_OF_RANGE_ERROR("3113", "잘못된 요청입니다."),
    AVAILABLETIME_REVERSE_CHRONOLOGY_ERROR("3114", "잘못된 요청입니다."),
    AVAILABLETIME_NOT_DIVIDED_BY_MINUTES_UNIT_ERROR("3115", "잘못된 요청입니다."),
    AVAILABLETIME_DURATION_NOT_MINUTES_UNIT_ERROR("3116", "잘못된 요청입니다."),

    APPOINTMENT_MEMBER_MISMATCHED_ERROR("3200", "접근 권한이 없는 요청입니다."),
    APPOINTMENT_NOT_FOUND_ERROR("3300", "요청한 리소스를 찾을 수 없습니다."),
    CACHED_BODY_ERROR("4100", "캐시바디"),

    INVALID_PROPERTY_ERROR("9900", ""),
    MORAK_ERROR("9901", ""),

    RUNTIME_ERROR("9902", "");

    public static final String APPOINTMENT_TIME_PERIOD_END_TIME_NOT_NULL = "201";

    private final String number;
    private final String information;
}
