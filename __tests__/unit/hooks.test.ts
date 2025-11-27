import { renderHook, act } from '@testing-library/react-native';

// Simple toggle hook test
describe('useToggle', () => {
  const useToggle = (initial = false): [boolean, () => void] => {
    const [value, setValue] = require('react').useState(initial);
    return [value, require('react').useCallback(() => setValue((v: boolean) => !v), [])];
  };

  it('should toggle value', () => {
    const { result } = renderHook(() => useToggle(false));
    expect(result.current[0]).toBe(false);
    act(() => { result.current[1](); });
    expect(result.current[0]).toBe(true);
  });
});
