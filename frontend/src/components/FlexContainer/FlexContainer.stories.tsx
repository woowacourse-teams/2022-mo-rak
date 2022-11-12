import FlexContainer from '@/components/FlexContainer/FlexContainer';

import theme from '@/styles/theme';
import styled from '@emotion/styled';
import { Meta, Story } from '@storybook/react';

export default {
  title: 'Reusable Components/FlexContainer',
  component: FlexContainer
} as Meta;

const StyledBox = styled.div`
  width: 10rem;
  height: 10rem;
  border: 1px solid ${theme.colors.BLACK_100};
`;
const DefaultTemplate: Story = (args) => (
  <FlexContainer {...args}>
    <StyledBox />
    <StyledBox />
    <StyledBox />
  </FlexContainer>
);

const Default = DefaultTemplate.bind({});

const ColumnTemplate: Story = (args) => (
  <FlexContainer flexDirection="column" {...args}>
    <StyledBox />
    <StyledBox />
    <StyledBox />
  </FlexContainer>
);
const Column = ColumnTemplate.bind({});

export { Default, Column };
