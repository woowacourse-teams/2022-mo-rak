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

    MEMBER_NOT_FOUND_ERROR("0300", "요청한 멤버를 찾을 수 없습니다."),

    TEAM_INVITATION_EXPIRED_ERROR("1100", "만료된 초대코드입니다."),
    TEAM_ALREADY_JOINED_ERROR("1101", "멤버가 그룹에 이미 속해있습니다."),
    TEAM_MEMBER_MISMATCHED_ERROR("1200", "멤버가 그룹에 속해있지 않습니다."),
    TEAM_NOT_FOUND_ERROR("1300", "요청한 그룹을 찾을 수 없습니다."),
    TEAM_INVITATION_NOT_FOUND_ERROR("1301", "요청한 그룹 초대코드를 찾을 수 없습니다."),

    POLL_ALREADY_CLOSED_ERROR("2100", "이미 마감된 투표입니다."),
    POLL_COUNT_OVER_ERROR("2101", "허용된 투표 개수를 넘었습니다."),
    POLL_TEAM_MISMATCHED_ERROR("2200", "투표가 그룹에 속해있지 않습니다."),
    POLL_MEMBER_MISMATCHED_ERROR("2201", "투표의 호스트가 아닙니다."),
    POLL_ITEM_MISMATCHED_ERROR("2202", "투표 선택 항목이 투표에 속해있지 않습니다."),
    POLL_NOT_FOUND_ERROR("2300", "요청한 투표를 찾을 수 없습니다."),
    POLL_ITEM_NOT_FOUND_ERROR("2301", "요청한 투표항목을 찾을 수 없습니다."),

    APPOINTMENT_ALREADY_CLOSED_ERROR("3100", "이미 마감된 약속잡기입니다."),
    APPOINTMENT_DUPLICATED_AVAILABLE_TIME_ERROR("3101", "약속잡기 선택에 중복된 시간이 있습니다."),
    APPOINTMENT_PAST_CREATE_ERROR("3102", "약속잡기의 마지막 날짜와 시간은 현재보다 과거일 수 없습니다.."),
    APPOINTMENT_DURATION_OVER_TIME_PERIOD_ERROR("3103", "진행 시간은 약속잡기 시간보다 짧을 수 없습니다."),
    APPOINTMENT_DURATION_NOT_MINUTES_UNIT_ERROR("3104", "약속잡기 진행시간은 30분 단위여야 합니다."),
    APPOINTMENT_DURATION_HOUR_OVER_DAY_ERROR("3105", "약속잡기 진행시간(시)은 24시간을 넘을 수 없습니다."),
    APPOINTMENT_DURATION_MINUTE_OVER_HOUR_ERROR("3106", "약속잡기 진행시간(분) 은 60분 이상일 수 없습니다."),
    APPOINTMENT_DURATION_MINUTE_RANGE_ERROR("3107", "약속잡기 진행시간은 30분에서 24시간 사이여야 합니다."),
    APPOINTMENT_PAST_DATE_CREATE_ERROR("3108", "과거 날짜의 약속잡기를 생성할 수 없습니다."),
    APPOINTMENT_DATE_REVERSE_CHRONOLOGY_ERROR("3109", "약속잡기 마지막 날짜는 시작 날짜보다 미래여야 합니다."),
    APPOINTMENT_TIME_REVERSE_CHRONOLOGY_ERROR("3110", "약속잡기 마지막 시각은 시작 시각보다 미래여야 합니다."),
    APPOINTMENT_NOT_DIVIDED_BY_MINUTES_UNIT_ERROR("3111", "약속잡기 시작시각, 마지막 시각은 30분 단위여야 합니다."),
    AVAILABLETIME_DATE_OUT_OF_RANGE_ERROR("3112", "약속잡기 선택 날짜가 약속잡기 날짜를 벗어났습니다."),
    AVAILABLETIME_TIME_OUT_OF_RANGE_ERROR("3113", "약속잡기 선택 시간이 약속잡기 시간을 벗어났습니다."),
    AVAILABLETIME_REVERSE_CHRONOLOGY_ERROR("3114", "약속잡기 마지막 날짜&시각은 현재 날짜&시각보다 미래여야 합니다."),
    AVAILABLETIME_NOT_DIVIDED_BY_MINUTES_UNIT_ERROR("3115", "약속잡기 선택 시각은 30분 단위여야 합니다."),
    AVAILABLETIME_DURATION_NOT_MINUTES_UNIT_ERROR("3116", "약속잡기 선택 진행 시간은 30분 단위여야 합니다."),
    APPOINTMENT_MEMBER_MISMATCHED_ERROR("3200", "멤버가 약속잡기의 호스트가 아닙니다."),
    APPOINTMENT_TEAM_MISMATCHED_ERROR("3201", "약속잡기가 그룹에 속해있지 않습니다."),
    APPOINTMENT_NOT_FOUND_ERROR("3300", "요청한 약속잡기를 찾을 수 없습니다."),

    INVALID_PROPERTY_ERROR("4000", "잘못된 값이 입력되었습니다."),
    CACHED_BODY_ERROR("4100", "캐시바디"),

    MORAK_ERROR("9901", ""),
    RUNTIME_ERROR("9902", "");

    private final String number;
    private final String information;
}
