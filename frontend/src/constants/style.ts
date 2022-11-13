const Z_INDEX = {
  MAX: 9999,
  DRAWER: 300,
  GLOBALBAR_FOOTBAR: 200,
  TOOLTIP: 100,
  SIDEBAR: 200
} as const;

const LAYOUT = {
  PAGE_WIDTH: 'calc(100% - 36.4rem)',
  MAIN_PAGE_PADDING: '6.4rem 20rem',
  MOBILE_PAGE_PADDING: '20rem 8rem'
} as const;

export { Z_INDEX, LAYOUT };
