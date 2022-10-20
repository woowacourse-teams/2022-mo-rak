const getLocalStorageItem = <T = unknown>(key: string): T => {
  const value = localStorage.getItem(key);

  return value ? JSON.parse(value) : null;
};

const getSessionStorageItem = <T = unknown>(key: string): T => {
  const value = sessionStorage.getItem(key);

  return value ? JSON.parse(value) : null;
};

const saveLocalStorageItem = <T = unknown>(key: string, value: T | Array<T>) =>
  localStorage.setItem(key, JSON.stringify(value));

const saveSessionStorageItem = <T = unknown>(key: string, value: T | Array<T>) =>
  sessionStorage.setItem(key, JSON.stringify(value));

const removeLocalStorageItem = (key: string) => localStorage.removeItem(key);
const removeSessionStorageItem = (key: string) => sessionStorage.removeItem(key);

export {
  getLocalStorageItem,
  saveLocalStorageItem,
  getSessionStorageItem,
  saveSessionStorageItem,
  removeLocalStorageItem,
  removeSessionStorageItem
};
