const BREAK_POINT = {
  NOT_MOBILE_MIN: 500
};

const mediaQuery = (size: string, styles: string) => `
  @media ${size} {
    ${styles};
  }
`;

const responsive = {
  mobile: (styles: string) => mediaQuery(`(max-width: ${BREAK_POINT.NOT_MOBILE_MIN - 1}px)`, styles)
};

export default responsive;
